package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.FieldException;
import com.example.sportsbook_application_backend.exception.NonexistentDataException;
import com.example.sportsbook_application_backend.exception.UpdateException;
import com.example.sportsbook_application_backend.exception.UserStatusException;
import com.example.sportsbook_application_backend.model.dto.slip.SlipDTO;
import com.example.sportsbook_application_backend.model.entity.*;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.BetRepository;
import com.example.sportsbook_application_backend.repository.SlipRepository;
import com.example.sportsbook_application_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class SlipServiceTest {
    @MockBean
    private SlipRepository slipRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BetRepository betRepository;
    @Autowired
    private SlipService slipService;

    @BeforeEach
    public void setUp() {
        User user = new User(1L,"Ivan","Popov","ivan55@abv.bg","1234","ivcho",200F, UserStatus.ACTIVE, Role.USER);
        User user2 = new User(2L,"Ivan","Popov","ivan55@abv.bg","1234","ivcho",200F, UserStatus.ACTIVE, Role.USER);
        User user3 = new User(3L,"Ivan","Popov","ivan55@abv.bg","1234","ivcho",200F, UserStatus.FROZEN, Role.USER);
        User user4 = new User(4L,"Ivan","Popov","ivan55@abv.bg","1234","ivcho",200F, UserStatus.ACTIVE, Role.USER);
        User user5 = new User(5L,"Ivan","Popov","ivan55@abv.bg","1234","ivcho",0F, UserStatus.ACTIVE, Role.USER);

        ArrayList<User> users=new ArrayList<>();
        users.add(user);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);

        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Finished",ResultType.TWO);
        Bet bet =new Bet(1L,event, Outcome.LOST, ResultType.TWO,1.5F);
        Bet bet2 =new Bet(2L,event, Outcome.PENDING,null,1.5F);
        Bet bet3 =new Bet(3L,event, Outcome.WON,ResultType.TWO,1.5F);

        ArrayList<Slip> slips=new ArrayList<>();
        Slip slip=new Slip(1L,user2,bet,150.20F,225.3F,Outcome.LOST);
        slips.add(slip);
        Slip slip2=new Slip(2L,user,bet2,150.20F,225.3F,Outcome.PENDING);
        slips.add(slip2);
        Slip slip3=new Slip(3L,user4,bet3,150.20F,225.3F,Outcome.PENDING);
        slips.add(slip3);

        for(User user1:users) {
            Mockito.when(userRepository.existsById(user1.getUserId()))
                    .thenReturn(true);
            Mockito.when(userRepository.findUserByUserId(user1.getUserId()))
                    .thenReturn(user1);
            ArrayList<Slip> expiredSlips=new ArrayList<>();
            ArrayList<Slip> userSlips=new ArrayList<>();
            ArrayList<Slip> pendingSlips=new ArrayList<>();
            for (Slip slip1:slips){
                if(slip1.getUser()==user1){
                    if(slip1.getOutcome()!=Outcome.PENDING)
                        expiredSlips.add(slip1);
                    else
                        pendingSlips.add(slip1);
                    userSlips.add(slip1);
                }
            }
            Mockito.when(slipRepository.getExpiredBetsOfUser(user1))
                    .thenReturn(expiredSlips);
            Mockito.when(slipRepository.countAllByUser(user1))
                    .thenReturn(userSlips.size());
            Mockito.when(slipRepository.getAllByUserAndOutcome(user1,Outcome.PENDING))
                    .thenReturn(pendingSlips);
            if(user1.getUserId()==4) {
                Mockito.when(slipRepository.countSlipsByUser(user1))
                        .thenReturn(userSlips.size());
            }
            else {
                Mockito.when(slipRepository.countSlipsByUser(user1))
                        .thenReturn(userSlips.size()+1);
            }
        }

        ArrayList<Bet> bets = new ArrayList<>();
        bets.add(bet);
        bets.add(bet2);
        for (Bet bet1 : bets) {
            Mockito.when(betRepository.existsById(bet1.getId()))
                    .thenReturn(true);
            Mockito.when(betRepository.getBetById(bet1.getId()))
                    .thenReturn(bet1);
        }

        Mockito.when(userRepository.getAllByStatus(UserStatus.ACTIVE))
                .thenReturn(users);
    }

    @Test
    void checkForExistingParams() {
        NonexistentDataException exception=assertThrowsExactly(NonexistentDataException.class,()->slipService.checkForExistingParams(6L,1L));
        assertEquals(exception.getMessage(),"User with id:6, does NOT exist in the database.");
        exception=assertThrowsExactly(NonexistentDataException.class,()->slipService.checkForExistingParams(1L,5L));
        assertEquals(exception.getMessage(),"Bet with id:5, does NOT exist in the database.");

        assertThatNoException().isThrownBy(()->slipService.checkForExistingParams(1L,1L));
    }

    @Test
    void resolveSlips() {
        assertThatNoException().isThrownBy(()->slipService.resolveSlips(LocalDate.now().toString()));
    }

    @Test
    void placeBet() {
        assertThrowsExactly(UserStatusException.class,()->slipService.placeBet(3L,1L,50F));
        FieldException exception = assertThrowsExactly(FieldException.class,()->slipService.placeBet(1L,1L,250F));
        assertEquals(exception.getMessage(),"Not enough balance. Wallet = 200.0");
        exception = assertThrowsExactly(FieldException.class,()->slipService.placeBet(1L,1L,100F));
        assertEquals(exception.getMessage(),"Bet with id:1 has already expired.");
        assertThatNoException().isThrownBy(()->slipService.placeBet(1L,2L,100F));
        assertThrowsExactly(UpdateException.class,()->slipService.placeBet(4L,2L,100F));

    }

    @Test
    void getBetHistoryByUserId() {
        NonexistentDataException exception=assertThrowsExactly(NonexistentDataException.class,()->slipService.getBetHistoryByUserId(6L));
        assertEquals(exception.getMessage(),"User with id:6 does NOT exist in the database.");
        exception=assertThrowsExactly(NonexistentDataException.class,()->slipService.getBetHistoryByUserId(1L));
        assertEquals(exception.getMessage(),"User with id:1 does NOT have any expired bets yet.");

        assertThatNoException().isThrownBy(()->slipService.getBetHistoryByUserId(2L));
        ArrayList<SlipDTO> found = slipService.getBetHistoryByUserId(2L);
        assertThat(found.size())
                .isEqualTo(1);

        assertThat(found.get(0).getBetId())
                .isEqualTo(1L);
        assertThat(found.get(0).getSlipId())
                .isEqualTo(1L);
        assertThat(found.get(0).getLeague())
                .isEqualTo("Premier league");
        assertThat(found.get(0).getCountry())
                .isEqualTo("England");
        assertThat(found.get(0).getType())
                .isEqualTo("League");
        assertThat(found.get(0).getSeason())
                .isEqualTo(2022);
        assertThat(found.get(0).getHomeTeam())
                .isEqualTo("Chelsea");
        assertThat(found.get(0).getAwayTeam())
                .isEqualTo("Arsenal");
        assertThat(found.get(0).getMatchStatus())
                .isEqualTo("Finished");
        assertThat(found.get(0).getMatchResult())
                .isEqualTo(ResultType.TWO);
        assertThat(found.get(0).getBetType())
                .isEqualTo(ResultType.TWO);
        assertThat(found.get(0).getBetOdd())
                .isEqualTo(1.5F);
        assertThat(found.get(0).getBetOutcome())
                .isEqualTo(Outcome.LOST);
        assertThat(found.get(0).getSlipStake())
                .isEqualTo(150.20F);
        assertThat(found.get(0).getSlipOutcome())
                .isEqualTo(Outcome.LOST);
        assertThat(found.get(0).getExpectedProfit())
                .isEqualTo(225.3F);
    }
}