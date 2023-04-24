package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.*;
import com.example.sportsbook_application_backend.model.dto.user.UserChangePasswordDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserLoginDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserRegistrationDTO;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private User mockedUser;//used in tests where methods of user entity need to be mocked
    @Autowired
    private UserService userService;

    @Autowired
    private UserCacheService userCacheService;

    @BeforeEach
    void setUp() {
        userService.evictAllCache();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testMockCreation(){
        assertNotNull(userRepository);
        assertNotNull(passwordEncoder);
        assertNotNull(mockedUser);
        assertNotNull(userService);
    }

    @Test
    void updateUser() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        userService.updateUser(user);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    void getUserByUsername() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        when(userRepository.findUserByUsername("gosho123")).thenReturn(user);
        assertEquals(user,userService.getUserByUsername("gosho123"));
        verify(userRepository,times(1)).findUserByUsername("gosho123");
        assertEquals(user.getRole(),userService.getUserByUsername("gosho123").getRole());
    }

    @Test
    void getUserById() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        when(userRepository.findUserByUserId(1L)).thenReturn(user);
        assertEquals(user,userService.getUserById(1L));
        verify(userRepository,times(1)).findUserByUserId(1L);
    }

    @Test
    void getAllUsersByStatus() {
        User user1 = new User(1L,"Ivan","Ivanov","ivan123@abv.bg","Az$um_IVAN123","ivan123",200F, UserStatus.ACTIVE, Role.USER);
        User user2 = new User(2L,"Mariq","Koleva","mariq@yahoo.com","pA$$_MARIQ","mariq.koleva",200F, UserStatus.ACTIVE, Role.USER);
        ArrayList<User> activeUsers = new ArrayList<>(Arrays.asList(user1, user2));
        when(userRepository.getAllByStatus(UserStatus.ACTIVE)).thenReturn(activeUsers);
        assertEquals(activeUsers,userService.getAllUsersByStatus(UserStatus.ACTIVE));
        verify(userRepository,times(1)).getAllByStatus(UserStatus.ACTIVE);
    }

    @Test
    void nameRegex() {
        assertTrue(userService.nameRegex("Todor"));
        assertTrue(userService.nameRegex("Yordanov"));
        assertTrue(userService.nameRegex("Teodor"));
        assertTrue(userService.nameRegex("Nikolov"));
        assertFalse(userService.nameRegex("Мария"));
        assertFalse(userService.nameRegex("PENCHEVA"));
        assertFalse(userService.nameRegex("kaloqn"));
        assertFalse(userService.nameRegex("Prodanov "));
        assertFalse(userService.nameRegex("012321432903423"));
        assertFalse(userService.nameRegex(""));
        assertFalse(userService.nameRegex(" "));
    }

    @Test
    void emailRegex() {
        assertTrue(userService.emailRegex("natoto01@abv.bg"));
        assertTrue(userService.emailRegex("yordanovtodor281@gmail.com"));
        assertTrue(userService.emailRegex("Pavel141@yahoo.com"));
        assertTrue(userService.emailRegex("marin.stoqnov@gmail.com"));
        assertFalse(userService.emailRegex("kamen.abv.bg"));
        assertFalse(userService.emailRegex("tom@yahoo"));
        assertFalse(userService.emailRegex("ivanov24.com"));
        assertFalse(userService.emailRegex("john@com"));
        assertFalse(userService.emailRegex(""));
        assertFalse(userService.emailRegex(" "));
    }

    @Test
    void usernameRegex() {
        assertTrue(userService.usernameRegex("pavel_todorov"));
        assertTrue(userService.usernameRegex("zdravk000_1.05"));
        assertTrue(userService.usernameRegex("yordanov5.0"));
        assertTrue(userService.usernameRegex("petq20stoqnova"));
        assertFalse(userService.usernameRegex("mAri@ a.02"));
        assertFalse(userService.usernameRegex("GEORGI#()-202"));
        assertFalse(userService.usernameRegex(""));
        assertFalse(userService.usernameRegex("захари18"));
        assertFalse(userService.usernameRegex(" marin"));
        assertFalse(userService.usernameRegex("   stoqn18    "));
    }

    @Test
    void passwordRegex() {
        assertTrue(userService.passwordRegex("ppRR0b@"));
        assertTrue(userService.passwordRegex("awbfg_!?fA9"));
        assertTrue(userService.passwordRegex("req_._2314A"));
        assertTrue(userService.passwordRegex("Birth_date_30.06.2001"));
        assertTrue(userService.passwordRegex("aB8232323342342-_+=1823exiT"));
        assertTrue(userService.passwordRegex("rT$@#_'(t)(r)(y)_&.......23231"));
        assertFalse(userService.passwordRegex("Qf**d**_')(_)(_.)232Z????????31233213"));
        assertFalse(userService.passwordRegex("pT$8"));
        assertFalse(userService.passwordRegex("1923121223123231"));
        assertFalse(userService.passwordRegex("Това.e.парола"));
        assertFalse(userService.passwordRegex("mSIDi02 13@#$%8765"));
        assertFalse(userService.passwordRegex("pPlK-54***1`~12©"));
        assertFalse(userService.passwordRegex(""));
        assertFalse(userService.passwordRegex(" "));
        assertFalse(userService.passwordRegex("     ._9locK      "));
    }

    @Test
    void validateRegistrationFields() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("","","","","");
        FieldException fieldException = assertThrows(FieldException.class,() -> userService.validateRegistrationFields(userRegistrationDTO));
        assertEquals("Please fill all fields.",fieldException.getMessage());


        userRegistrationDTO.setFirstName("georgi");
        userRegistrationDTO.setLastName("Ivanov");
        userRegistrationDTO.setEmail("gosho123@abv.bg");
        userRegistrationDTO.setUsername("gosho123");
        userRegistrationDTO.setPassword("Az$um_GOSHO123");
        fieldException = assertThrows(FieldException.class,() -> userService.validateRegistrationFields(userRegistrationDTO));
        assertEquals("The first name should have between 3 and 20 letters[a-z] starting with a capital letter.",fieldException.getMessage());

        userRegistrationDTO.setFirstName("Georgi");
        userRegistrationDTO.setLastName("ivanov");
        fieldException = assertThrows(FieldException.class,() -> userService.validateRegistrationFields(userRegistrationDTO));
        assertEquals("The last name should have between 3 and 20 letters[a-z] starting with a capital letter.",fieldException.getMessage());

        userRegistrationDTO.setLastName("Ivanov");
        userRegistrationDTO.setEmail("gosho.abv.bg");
        fieldException = assertThrows(FieldException.class,() -> userService.validateRegistrationFields(userRegistrationDTO));
        assertEquals("The email should have between 6 and 47 symbols and consists of valid email domain.",fieldException.getMessage());

        userRegistrationDTO.setEmail("gosho123@abv.bg");
        userRegistrationDTO.setUsername("гошо123");
        fieldException = assertThrows(FieldException.class,() -> userService.validateRegistrationFields(userRegistrationDTO));
        assertEquals("The username should have between 4 and 20 symbols.{[a-z],[0-9],(_),(.)}",fieldException.getMessage());

        userRegistrationDTO.setUsername("gosho123");
        userRegistrationDTO.setPassword("proba1234");
        fieldException = assertThrows(FieldException.class,() -> userService.validateRegistrationFields(userRegistrationDTO));
        assertEquals("The password should have between 7 and 30 symbols {[a-z],[0-9],[@#$%^&+=_*~!)(./:;<>?{}|`',-]}. One capital, one small letter, one digit and one special symbol should be used at least once.",fieldException.getMessage());
        userRegistrationDTO.setPassword("Az$um_GOSHO123");

        when(userRepository.existsUserByEmail("gosho123@abv.bg")).thenReturn(true);
        DuplicateEmailException duplicateEmailException = assertThrows(DuplicateEmailException.class,() -> userService.validateRegistrationFields(userRegistrationDTO));
        assertEquals("The email you entered already exists.",duplicateEmailException.getMessage());
        when(userRepository.existsUserByEmail("gosho123@abv.bg")).thenReturn(false);
        verify(userRepository,times(1)).existsUserByEmail("gosho123@abv.bg");

        userRegistrationDTO.setEmail("asd@asd.asdc");
        when(userRepository.findUserByUsername("gosho123")).thenReturn(new User());
        DuplicateUsernameException duplicateUsernameException = assertThrows(DuplicateUsernameException.class,() -> userService.validateRegistrationFields(userRegistrationDTO));
        assertEquals("The username you entered already exists.",duplicateUsernameException.getMessage());
        userService.evictAllCache();
        when(userRepository.findUserByUsername("gosho123")).thenReturn(null);

        assertDoesNotThrow(() -> userService.validateRegistrationFields(userRegistrationDTO));
    }

    @Test
    void validateLoginFields() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("","");
        FieldException fieldException = assertThrows(FieldException.class,() -> userService.validateLoginFields(userLoginDTO));
        assertEquals("Please fill all fields.",fieldException.getMessage());

        userLoginDTO.setUsername("гошо123");
        userLoginDTO.setPassword("p@rolata_na_Gosho123");
        fieldException = assertThrows(FieldException.class,() -> userService.validateLoginFields(userLoginDTO));
        assertEquals("The username should have between 4 and 20 symbols.{[a-z],[0-9],(_),(.)}",fieldException.getMessage());

        userLoginDTO.setUsername("gosho123");
        userLoginDTO.setPassword("1234проба");
        fieldException = assertThrows(FieldException.class,() -> userService.validateLoginFields(userLoginDTO));
        assertEquals("The password should have between 7 and 30 symbols {[a-z],[0-9],[@#$%^&+=_*~!)(./:;<>?{}|`',-]}. One capital, one small letter, one digit and one special symbol should be used at least once.",fieldException.getMessage());

        userLoginDTO.setPassword("p@rolata_na_Gosho123");
        assertDoesNotThrow(() -> userService.validateLoginFields(userLoginDTO));
    }

    @Test
    void validateEditFields() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        UserDTO userDTO = new UserDTO(null,"","","","",null);

        ProfileMismatchException profileMismatchException = assertThrows(ProfileMismatchException.class,() -> userService.validateEditFields(user,userDTO));
        assertEquals("Access denied!",profileMismatchException.getMessage());
        userDTO.setId(1L);

        FieldException fieldException = assertThrows(FieldException.class,() -> userService.validateEditFields(user,userDTO));
        assertEquals("Please fill all fields.",fieldException.getMessage());

        userDTO.setFirstName("Георги");
        userDTO.setLastName("Ivanov");
        userDTO.setEmail("gosho123@abv.bg");
        userDTO.setUsername("gosho123");
        fieldException = assertThrows(FieldException.class,() -> userService.validateEditFields(user,userDTO));
        assertEquals("The first name should have between 3 and 20 letters[a-z] starting with a capital letter.",fieldException.getMessage());

        userDTO.setFirstName("Georgi");
        userDTO.setLastName("ivanov");
        fieldException = assertThrows(FieldException.class,() -> userService.validateEditFields(user,userDTO));
        assertEquals("The last name should have between 3 and 20 letters[a-z] starting with a capital letter.",fieldException.getMessage());

        userDTO.setLastName("Ivanov");
        userDTO.setEmail("gosho.gmail.com");
        fieldException = assertThrows(FieldException.class,() -> userService.validateEditFields(user,userDTO));
        assertEquals("The email should have between 6 and 47 symbols and consists of valid email domain.",fieldException.getMessage());

        userDTO.setEmail("gosho1234@abv.bg");
        userDTO.setUsername("gosho 01");
        fieldException = assertThrows(FieldException.class,() -> userService.validateEditFields(user,userDTO));
        assertEquals("The username should have between 4 and 20 symbols.{[a-z],[0-9],(_),(.)}",fieldException.getMessage());
        userDTO.setUsername("gosho1234");


        when(userRepository.existsUserByEmail("gosho1234@abv.bg")).thenReturn(true);
        DuplicateEmailException duplicateEmailException = assertThrows(DuplicateEmailException.class,() -> userService.validateEditFields(user,userDTO));
        assertEquals("The email you entered already exists.",duplicateEmailException.getMessage());
        when(userRepository.existsUserByEmail("gosho1234@abv.bg")).thenReturn(false);
        verify(userRepository,times(1)).existsUserByEmail("gosho1234@abv.bg");
        userService.evictAllCache();
        when(userRepository.findUserByUsername("gosho1234")).thenReturn(new User());
        DuplicateUsernameException duplicateUsernameException = assertThrows(DuplicateUsernameException.class,() -> userService.validateEditFields(user,userDTO));
        assertEquals("The username you entered already exists.",duplicateUsernameException.getMessage());
        when(userRepository.findUserByUsername("gosho1234")).thenReturn(null);
        userService.evictAllCache();
        assertDoesNotThrow(() -> userService.validateEditFields(user,userDTO));
    }

    @Test
    void isEmailExists() {
        when(userRepository.existsUserByEmail("gosho123@abv.bg")).thenReturn(true);
        assertTrue(userService.isEmailExists("gosho123@abv.bg"));
        verify(userRepository,times(1)).existsUserByEmail("gosho123@abv.bg");

        when(userRepository.existsUserByEmail("mariq@abv.bg")).thenReturn(false);
        assertFalse(userService.isEmailExists("mariq@abv.bg"));
        verify(userRepository,times(1)).existsUserByEmail("mariq@abv.bg");
    }

    @Test
    void isUsernameExists() {
        when(userRepository.findUserByUsername("gosho123")).thenReturn(new User());
        assertTrue(userService.isUsernameExists("gosho123"));
        userService.evictAllCache();

        when(userRepository.findUserByUsername("mariq18")).thenReturn(null);
        assertFalse(userService.isUsernameExists("mariq18"));
    }

    @Test
    void isUserExists() {
        when(userRepository.findUserByUserId(1L)).thenReturn(new User());
        assertTrue(userService.isUserExists(1L));

        when(userRepository.findUserByUserId(2L)).thenReturn(null);
        assertFalse(userService.isUserExists(2L));
    }

    @Test
    void createUser() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("Georgi","Ivanov","gosho123@abv.bg","gosho123","Az$um_GOSHO123");
        User user = User.builder()
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .email(userRegistrationDTO.getEmail())
                .username(userRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .balance(200F)
                .status(UserStatus.ACTIVE)
                .role(Role.USER)
                .build();

        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(null);
        when(mockedUser.getUsername()).thenReturn(user.getUsername());
        UpdateException updateException = assertThrows(UpdateException.class,() -> userService.createUser(userRegistrationDTO));
        assertEquals("Error when creating user registration. Please try again.",updateException.getMessage());

        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(new User());
        assertDoesNotThrow(() -> userService.createUser(userRegistrationDTO));
    }

    @Test
    void checkUserCredentials() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        UserLoginDTO userLoginDTO = new UserLoginDTO("gosho123","Az$um_GOSHO123");
        when(userRepository.findUserByUsername("gosho123")).thenReturn(null);
        WrongCredentialsException wrongCredentialsException = assertThrows(WrongCredentialsException.class,() -> userService.checkUserCredentials(userLoginDTO));
        assertEquals("Wrong credentials!",wrongCredentialsException.getMessage());

        when(userRepository.findUserByUsername("gosho123")).thenReturn(user);
        wrongCredentialsException = assertThrows(WrongCredentialsException.class,() -> userService.checkUserCredentials(userLoginDTO));
        assertEquals("Wrong credentials!",wrongCredentialsException.getMessage());

        userService.evictAllCache();
        when(passwordEncoder.matches(userLoginDTO.getPassword(), userLoginDTO.getPassword())).thenReturn(true);
        assertDoesNotThrow(() -> userService.checkUserCredentials(userLoginDTO));
    }

    @Test
    void editUser() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        UserDTO userDTO = new UserDTO(1L,"Georgi","Georgiev","gosho123@abv.bg","Az$um_GOSHO123",200F);
        User user1 = userService.editUser(user,userDTO);
        assertEquals(user,user1);
    }

    @Test
    void changePassword() {
        when(userRepository.findUserByUserId(1L)).thenReturn(mockedUser);
        assertEquals(mockedUser,userService.getUserById(1L));
        verify(userRepository,times(1)).findUserByUserId(1L);

        UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO(1L,"Az$um_GOSHO123","Az$um_GOSHO12345");
        mockedUser.setPassword(passwordEncoder.encode(userChangePasswordDTO.getNewPassword()));
        when(mockedUser.getUsername()).thenReturn("test");
        when(userRepository.findUserByUsername("test")).thenReturn(mockedUser);

        when(mockedUser.getPassword()).thenReturn("Az$um_GOSHO12345","Az$um_GOSHO123");
        UpdateException updateException = assertThrows(UpdateException.class,() -> userService.changePassword(mockedUser,userChangePasswordDTO));
        assertEquals("Unsuccessful password update. Please try again.",updateException.getMessage());

        when(mockedUser.getPassword()).thenReturn("Az$um_GOSHO12345","Az$um_GOSHO12345");
        assertDoesNotThrow(() -> userService.changePassword(mockedUser,userChangePasswordDTO));
    }

    @Test
    void validatePasswordChange() {
        User user = new User(2L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO1234","gosho123",200F, UserStatus.ACTIVE, Role.USER);

        UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO(1L,"12345","12345");
        ProfileMismatchException profileMismatchException = assertThrows(ProfileMismatchException.class,() -> userService.validatePasswordChange(user,userChangePasswordDTO));
        assertEquals("Access denied!",profileMismatchException.getMessage());

        user.setUserId(1L);

        FieldException fieldException = assertThrows(FieldException.class,() -> userService.validatePasswordChange(user,userChangePasswordDTO));
        assertEquals("The password should have between 7 and 30 symbols {[a-z],[0-9],[@#$%^&+=_*~!)(./:;<>?{}|`',-]}. One capital, one small letter, one digit and one special symbol should be used at least once.",fieldException.getMessage());

        userChangePasswordDTO.setOldPassword("Az$um_GOSHO123");
        userChangePasswordDTO.setNewPassword("Az$um_GOSHO1234");
        WrongCredentialsException wrongCredentialsException = assertThrows(WrongCredentialsException.class,() -> userService.validatePasswordChange(user,userChangePasswordDTO));
        assertEquals("Wrong old password. Please provide a valid password.",wrongCredentialsException.getMessage());
        userChangePasswordDTO.setOldPassword("Az$um_GOSHO1234");
        when(passwordEncoder.matches(userChangePasswordDTO.getOldPassword(), user.getPassword())).thenReturn(true);

        DuplicatePasswordException duplicatePasswordException = assertThrows(DuplicatePasswordException.class,() -> userService.validatePasswordChange(user,userChangePasswordDTO));
        assertEquals("Please enter a password different from the old one.",duplicatePasswordException.getMessage());

        userChangePasswordDTO.setNewPassword("Az$um_GOSHO12345");
        assertDoesNotThrow(() -> userService.validatePasswordChange(user,userChangePasswordDTO));
    }
}