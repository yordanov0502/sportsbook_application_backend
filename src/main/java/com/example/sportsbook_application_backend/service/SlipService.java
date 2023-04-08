package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.FieldException;
import com.example.sportsbook_application_backend.exception.NonexistentDataException;
import com.example.sportsbook_application_backend.exception.UpdateException;
import com.example.sportsbook_application_backend.exception.UserStatusException;
import com.example.sportsbook_application_backend.model.dto.slip.SlipDTO;
import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Slip;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.model.mapper.SlipMapper;
import com.example.sportsbook_application_backend.repository.SlipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SlipService {

    private final SlipRepository slipRepository;
    private final UserService userService;
    private final BetService betService;
    private final SlipMapper slipMapper;

    public void checkForExistingParams(Long userId, Long betId){
        if(!userService.isUserExists(userId))
            throw new NonexistentDataException("User with id:"+userId+", does NOT exist in the database.");

        if(!betService.isBetExists(betId))
            throw new NonexistentDataException("Bet with id:"+betId+", does NOT exist in the database.");
    }

    public void resolveSlips(String  date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        ArrayList<User> users = userService.getAllUsersByStatus(UserStatus.ACTIVE);
        for(User user:users)
        {
           ArrayList<Slip> slipsOfUser = slipRepository.getAllByUserAndOutcome(user,Outcome.PENDING);
           for(Slip slip:slipsOfUser)
           {
               if(slip.getBet().getEvent().getDate().equals(localDate))
               {
                   slip.setOutcome(slip.getBet().getOutcome());
                   slipRepository.save(slip);
                   if(slip.getOutcome().equals(Outcome.WON))
                   {
                       user.setBalance(user.getBalance()+(slip.getStake()*slip.getBet().getOdd()));
                       userService.updateUser(user);
                   }
               }
           }
           //if a user has no slips with status PENDING and has 0$ balance, then his account becomes FROZEN and no longer can place bets(slips)
            if(slipRepository.getAllByUserAndOutcome(user,Outcome.PENDING).isEmpty() && user.getBalance()==0)
            {
                user.setStatus(UserStatus.FROZEN);
                userService.updateUser(user);
            }
        }
    }


    //creates a slip (associated with existing user and bet)
    public void placeBet(Long userId,Long betId,Float stake)
    {
        User user = userService.getUserById(userId);

        if(user.getStatus().equals(UserStatus.FROZEN))
            throw new UserStatusException("User with id:"+user.getUserId()+" cannot place a bet, because the account is frozen.(balance=0)");

        if(stake>user.getBalance())
            throw new FieldException("Not enough balance. Wallet = "+user.getBalance());

        Bet bet = betService.getBetById(betId);
        if(!bet.getOutcome().equals(Outcome.PENDING))
            throw new FieldException("Bet with id:"+bet.getId()+" has already expired.");

        float expectedProfit = stake * bet.getOdd();
        int slipQuantityBefore= slipRepository.countAllByUser(user);
        Slip slip = new Slip(null,user,bet,stake,expectedProfit,Outcome.PENDING);
        slipRepository.save(slip);
        int slipQuantityAfter = slipRepository.countSlipsByUser(user);

        if(slipQuantityBefore!=slipQuantityAfter)//checks if new slip was added (relating certain user)
        {
            user.setBalance(user.getBalance()-stake);
            userService.updateUser(user);
        }
        else throw new UpdateException("Slip was NOT created. Please try again.");
    }

    public ArrayList<SlipDTO> getBetHistoryByUserId(Long id){

        if(!userService.isUserExists(id))
            throw new NonexistentDataException("User with id:"+id+" does NOT exist in the database.");

        ArrayList<Slip> expiredSlips = slipRepository.getExpiredBetsOfUser(userService.getUserById(id));
        if(expiredSlips.isEmpty())
            throw new NonexistentDataException("User with id:"+id+" does NOT have any expired bets yet.");

        ArrayList<SlipDTO> slipDTOS = new ArrayList<>();
        for(Slip slip: expiredSlips)
        {
            slipDTOS.add(slipMapper.mapToSlipDTO(slip));
        }

        return slipDTOS;
    }

}