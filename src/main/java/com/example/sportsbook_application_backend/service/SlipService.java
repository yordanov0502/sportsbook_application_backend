package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Slip;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.SlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class SlipService {

    @Autowired
    private SlipRepository slipRepository;
    @Autowired
    private UserService userService;

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
           if(slipRepository.getAllByUserAndOutcome(user,Outcome.PENDING).equals(null) && user.getBalance()<=0)
           {
             user.setStatus(UserStatus.FROZEN);
             userService.updateUser(user);
           }
        }
    }
}