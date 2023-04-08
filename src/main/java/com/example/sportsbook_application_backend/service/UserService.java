package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.*;
import com.example.sportsbook_application_backend.model.dto.user.UserChangePasswordDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserLoginDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserRegistrationDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserDTO;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void updateUser(User user) {userRepository.save(user);}

    public User getUserByUsername(String username) {return userRepository.findUserByUsername(username);}

    public User getUserById(Long id) {return userRepository.findUserByUserId(id);}

    public ArrayList<User> getAllUsersByStatus(UserStatus status) {return userRepository.getAllByStatus(status);}


    private boolean nameRegex(String firstName) {
        String regex = "^[A-Z]{1}([a-z]{2,20})$";

        Pattern p = Pattern.compile(regex);
        if(firstName == null) {return false;}
        else
        {
            Matcher m = p.matcher(firstName);
            return m.matches();
        }
    }

    private boolean emailRegex(String email) {
        String regex = "^[\\w-\\.]{1,30}@([\\w-]{1,10}\\.)+[\\w-]{2,5}$";

        Pattern p = Pattern.compile(regex);
        if(email == null) {return false;}
        else
        {
            Matcher m = p.matcher(email);
            return m.matches();
        }
    }

    private boolean usernameRegex(String username) {
        String regex = "[a-z0-9._]{4,20}$";

        Pattern p = Pattern.compile(regex);
        if(username == null) {return false;}
        else
        {
            Matcher m = p.matcher(username);
            return m.matches();
        }
    }

    private boolean passwordRegex(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_*~!)(./:;<>?{}|`',-])(?=\\S+$).{7,30}$";

        Pattern p = Pattern.compile(regex);
        if(password == null) {return false;}
        else if(StringUtils.isAsciiPrintable(password))//checks if the password contains only of ASCII symbols
        {
            Matcher m = p.matcher(password);
            return m.matches();//checks if the password matches the regex
        }
        else {return false;}
    }

    public void validateRegistrationFields(UserRegistrationDTO userRegistrationDTO) //validate fields in registration form
    {
        if(Objects.equals(userRegistrationDTO.getFirstName(), "")||Objects.equals(userRegistrationDTO.getLastName(), "")||Objects.equals(userRegistrationDTO.getEmail(), "")||Objects.equals(userRegistrationDTO.getUsername(), "")||Objects.equals(userRegistrationDTO.getPassword(), ""))
            throw new FieldException("Please fill all fields.");

        if(!nameRegex(userRegistrationDTO.getFirstName()))
            throw new FieldException("The first name should have between 3 and 20 letters[a-z] starting with a capital letter.");

        if(!nameRegex(userRegistrationDTO.getLastName()))
            throw new FieldException("The last name should have between 3 and 20 letters[a-z] starting with a capital letter.");

        if(!emailRegex(userRegistrationDTO.getEmail()))
            throw new FieldException("The email should have between 6 and 47 symbols.");

        if(!usernameRegex(userRegistrationDTO.getUsername()))
            throw new FieldException("The username should have between 4 and 20 symbols.{[a-z],[0-9],(_),(.)}");

        if(!passwordRegex(userRegistrationDTO.getPassword()))
            throw new FieldException("The password should have between 7 and 30 symbols {[a-z],[0-9],[@#$%^&+=_*~!)(./:;<>?{}|`',-]}. One capital, one small letter, one digit and one special symbol should be used at least once.");

        if(isEmailExists(userRegistrationDTO.getEmail()))
            throw new DuplicateEmailException("The email you entered already exists.");

        if(isUsernameExists(userRegistrationDTO.getUsername()))
            throw new DuplicateUsernameException( "The username you entered already exists.");
    }

    public void validateLoginFields(UserLoginDTO userLoginDTO) //validate fields in login form
    {
        if (Objects.equals(userLoginDTO.getUsername(), "") || Objects.equals(userLoginDTO.getPassword(), ""))
            throw new FieldException("Please fill all fields.");

        if(!usernameRegex(userLoginDTO.getUsername()))
            throw new FieldException("The username should have between 4 and 20 symbols.{[a-z],[0-9],(_),(.)}");

        if(!passwordRegex(userLoginDTO.getPassword()))
            throw new FieldException("The password should have between 7 and 30 symbols {[a-z],[0-9],[@#$%^&+=_*~!)(./:;<>?{}|`',-]}. One capital, one small letter, one digit and one special symbol should be used at least once.");
    }

    public void validateEditFields(UserDTO userDTO) //validate fields in user edit info form
    {
        User user = getUserById(userDTO.getId());

        if(Objects.equals(userDTO.getFirstName(), "")||Objects.equals(userDTO.getLastName(), "")||Objects.equals(userDTO.getEmail(), "")||Objects.equals(userDTO.getUsername(), ""))
            throw new FieldException("Please fill all fields.");

        if(!nameRegex(userDTO.getFirstName()))
            throw new FieldException("The first name should have between 3 and 20 letters[a-z] starting with a capital letter.");

        if(!nameRegex(userDTO.getLastName()))
            throw new FieldException ("The last name should have between 3 and 20 letters[a-z] starting with a capital letter.");

        if(!emailRegex(userDTO.getEmail()))
            throw new FieldException ("The email should have between 6 and 47 symbols.");

        if(!usernameRegex(userDTO.getUsername()))
            throw new FieldException("The username should have between 4 and 20 symbols.{[a-z],[0-9],(_),(.)}");

        if(!user.getEmail().equals(userDTO.getEmail()) && isEmailExists(userDTO.getEmail()))
            throw new DuplicateEmailException("The email you entered already exists.");

        if(!user.getUsername().equals(userDTO.getUsername()) && isUsernameExists(userDTO.getUsername()))
            throw new DuplicateUsernameException("The username you entered already exists.");
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public boolean isUserExists(Long id){
        return userRepository.existsById(id);
    }

    public void createUser(UserRegistrationDTO userRegistrationDTO){

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

        userRepository.save(user);

        //checks if user registration was successful(user was added to database)
        if(!isUsernameExists(user.getUsername()))
            throw new UpdateException("Error when creating user registration. Please try again.");
    }

    public void checkUserCredentials(UserLoginDTO userLoginDTO){
        Optional<User> user = userRepository.findByUsername(userLoginDTO.getUsername());
        if (!(user.isPresent() && passwordEncoder.matches(userLoginDTO.getPassword(), user.get().getPassword())))
            throw new WrongCredentialsException("Wrong credentials!");
    }

    public User editUser(UserDTO userDTO){
        User user=getUserById(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        userRepository.save(user);
        return user;
    }

    public void changePassword(UserChangePasswordDTO userChangePasswordDTO){
        User user=getUserById(userChangePasswordDTO.getId());
        user.setPassword(passwordEncoder.encode(userChangePasswordDTO.getNewPassword()));
        userRepository.save(user);

        if(!user.getPassword().equals(getUserById(userChangePasswordDTO.getId()).getPassword()))
            throw new UpdateException("Unsuccessful password update. Please try again.");
    }

    public void validatePasswordChange(UserChangePasswordDTO userChangePasswordDTO)
    {
        User user = getUserById(userChangePasswordDTO.getId());

        if(!passwordRegex(userChangePasswordDTO.getOldPassword()) || !passwordRegex(userChangePasswordDTO.getNewPassword()))
            throw new FieldException("The password should have between 7 and 30 symbols {[a-z],[0-9],[@#$%^&+=_*~!)(./:;<>?{}|`',-]}. One capital, one small letter, one digit and one special symbol should be used at least once.");

        if(!passwordEncoder.matches(userChangePasswordDTO.getOldPassword(), user.getPassword()))
            throw new WrongCredentialsException("Wrong old password. Please provide a valid password.");

        if(passwordEncoder.matches(userChangePasswordDTO.getNewPassword(),user.getPassword()))
            throw new DuplicatePasswordException("Please enter a password different from the old one.");
    }
}