package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.*;
import com.example.sportsbook_application_backend.model.dto.UserChangePasswordDTO;
import com.example.sportsbook_application_backend.model.dto.UserLoginDTO;
import com.example.sportsbook_application_backend.model.dto.UserRegistrationDTO;
import com.example.sportsbook_application_backend.model.dto.UserDTO;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String username) {return userRepository.findUserByUsername(username);}

    public User getUserById(Long id) {return userRepository.findUserByUserId(id);}

    public static boolean validateName(String firstName) {
        String regex = "^[A-Z]{1}([a-z]{2,20})$";

        Pattern p = Pattern.compile(regex);
        if(firstName == null) {return false;}
        else
        {
            Matcher m = p.matcher(firstName);
            return m.matches();
        }
    }

    public static boolean validateEmail(String email) {
        String regex = "^[\\w-\\.]{1,30}@([\\w-]{1,10}\\.)+[\\w-]{2,5}$";

        Pattern p = Pattern.compile(regex);
        if(email == null) {return false;}
        else
        {
            Matcher m = p.matcher(email);
            return m.matches();
        }
    }

    public static boolean validateUsername(String username) {
        String regex = "[a-z0-9._]{4,20}$";

        Pattern p = Pattern.compile(regex);
        if(username == null) {return false;}
        else
        {
            Matcher m = p.matcher(username);
            return m.matches();
        }
    }

    public boolean validatePassword(String password) {
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

        if(!validateName(userRegistrationDTO.getFirstName()))
            throw new FieldException("The first name should have between 3 and 20 letters[a-z] starting with a capital letter.");

        if(!validateName(userRegistrationDTO.getLastName()))
            throw new FieldException("The last name should have between 3 and 20 letters[a-z] starting with a capital letter.");

        if(!validateEmail(userRegistrationDTO.getEmail()))
            throw new FieldException("The email should have between 6 and 47 symbols.");

        if(!validateUsername(userRegistrationDTO.getUsername()))
            throw new FieldException("The username should have between 4 and 20 symbols.{[a-z],[0-9],(_),(.)}");

        if(!validatePassword(userRegistrationDTO.getPassword()))
            throw new FieldException("The password should have between 7 and 30 symbols {[a-z],[0-9],[@#$%^&+=_*~!)(./:;<>?{}|`',-]}.\nOne capital, one small letter, one digit and one special symbol should be used at least once.");

        if(isEmailExists(userRegistrationDTO.getEmail()))
            throw new DuplicateEmailException("The email you entered already exists.");

        if(isUsernameExists(userRegistrationDTO.getUsername()))
            throw new DuplicateUsernameException( "The username you entered already exists.");
    }

    public void validateLoginFields(UserLoginDTO userLoginDTO) //validate fields in login form
    {
        if (Objects.equals(userLoginDTO.getUsername(), "") || Objects.equals(userLoginDTO.getPassword(), ""))
            throw new FieldException("Please fill all fields.");

        if(!validateUsername(userLoginDTO.getUsername()))
            throw new FieldException("The username should have between 4 and 20 symbols.{[a-z],[0-9],(_),(.)}");

        if(!validatePassword(userLoginDTO.getPassword()))
            throw new FieldException("The password should have between 7 and 30 symbols {[a-z],[0-9],[@#$%^&+=_*~!)(./:;<>?{}|`',-]}. One capital, one small letter, one digit and one special symbol should be used at least once.");
    }

    public void validateEditFields(UserDTO userDTO) //validate fields in user edit info form
    {
        User user = getUserById(userDTO.getId());

        if(Objects.equals(userDTO.getFirstName(), "")||Objects.equals(userDTO.getLastName(), "")||Objects.equals(userDTO.getEmail(), "")||Objects.equals(userDTO.getUsername(), ""))
            throw new FieldException("Please fill all fields.");

        if(!validateName(userDTO.getFirstName()))
            throw new FieldException("The first name should have between 3 and 20 letters[a-z] starting with a capital letter.");

        if(!validateName(userDTO.getLastName()))
            throw new FieldException ("The last name should have between 3 and 20 letters[a-z] starting with a capital letter.");

        if(!validateEmail(userDTO.getEmail()))
            throw new FieldException ("The email should have between 6 and 47 symbols.");

        if(!validateUsername(userDTO.getUsername()))
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

    public void createUser(UserRegistrationDTO userRegistrationDTO){
        String password= userRegistrationDTO.getPassword();
        User user=new User();
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setUsername(userRegistrationDTO.getUsername());
        user.setHash(passwordEncoder.encode(password));
        user.setBalance(200F);
        user.setStatus("active");
        userRepository.save(user);

        //checks if user registration was successful
        if(!isUsernameExists(user.getUsername()))
        throw new UpdateException("Error when creating user registration. Please try again.");
    }


    public void editUser(UserDTO userDTO){
        User user=getUserById(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        userRepository.save(user);
    }

    public void changePassword(UserChangePasswordDTO userChangePasswordDTO){
        User user=getUserById(userChangePasswordDTO.getId());
        user.setHash(passwordEncoder.encode(userChangePasswordDTO.getNewPassword()));
        userRepository.save(user);

        if(!user.getHash().equals(getUserById(userChangePasswordDTO.getId()).getHash()))
        throw new UpdateException("Unsuccessful password update. Please try again.");
    }

    public void checkUserCredentials(UserLoginDTO userLoginDTO){
        if(userRepository.existsUserByUsername(userLoginDTO.getUsername()))
        {
            User user = userRepository.findUserByUsername(userLoginDTO.getUsername());
            if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getHash()))
                throw new WrongCredentialsException("Wrong credentials!");
        }
        else {throw new WrongCredentialsException("Wrong credentials!");}
    }

    public void validatePasswordChange(UserChangePasswordDTO userChangePasswordDTO)
    {
        User user = getUserById(userChangePasswordDTO.getId());

        if(!passwordEncoder.matches(userChangePasswordDTO.getOldPassword(), user.getHash()))
            throw new WrongCredentialsException("Wrong old password. Please provide a valid password.");

        if(passwordEncoder.matches(userChangePasswordDTO.getNewPassword(),user.getHash()))
            throw new DuplicatePasswordException("Please enter a password different from the old one.\"");
    }
}