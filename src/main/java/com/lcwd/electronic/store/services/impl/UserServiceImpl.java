package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositiories.RoleRepository;
import com.lcwd.electronic.store.repositiories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import lombok.Builder;
import com.lcwd.electronic.store.entities.User;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {
 @Autowired
 private UserRepository userRepository;
 @Autowired
 private ModelMapper mapper;
 @Value("${user.profile.image.path}")
 private  String imagePath;
 private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
 @Autowired
 private RoleRepository roleRepository;
 @Autowired
 private PasswordEncoder passwordEncoder;
    @Override
    public UserDto createUser(UserDto userDto) {
        String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user=dtoToEntity(userDto);
        //password encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //get the normal role
        Role role =new Role();
        role.setRoleId(UUID.randomUUID().toString());
//        role.setName("ROLE_NORMAL");
        role.setName(userDto.getRoles().get(0).getName());
        Role roleNormal =roleRepository.findByName(userDto.getRoles().get(0).getName()).orElse(role);
        user.setRoles(List.of(roleNormal));


        User savedUser=userRepository.save(user);
       UserDto newDto=entityToDto(savedUser);
        return newDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
       User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found with given id"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        //assign normal role to user
        //by detail jo bhi api se user banega usko ham log normal user banayenge
        //save data
        User updatedUser= userRepository.save(user);
        UserDto updatedDto=entityToDto(updatedUser);

        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with given id"));
        //delete user profile image
        //images/user/abc.png
        String fullPath =imagePath+user.getImageName();
        try {
            Path path = Paths.get(fullPath);
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("Image deleted successfully.");
            } else {
                logger.warn("Image file does not exist: {}", fullPath);
            }
        } catch (IOException e) {
            logger.error("Error deleting image file: {}", e.getMessage());
        }


        //delete user
       userRepository.delete(user);
    }



    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        //page number default sta
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page =userRepository.findAll(pageable);
        PageableResponse<UserDto> response=Helper.getPageableResponse(page,UserDto.class);


        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found with given id !!"));

        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("user not found with given email  !!"));
        return entityToDto(user);
    }


    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users=userRepository.findByNameContaining(keyword);
      List<UserDto> dtoList= (List<UserDto>) users.stream().map(user -> entityToDto(user));
      return dtoList;
    }
    private UserDto entityToDto(User savedUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .imageName(savedUser.getImageName())
//                .gender(savedUser.getGender())
//                .build();
        return mapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user= User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
        return mapper.map(userDto,User.class);
    }
}
