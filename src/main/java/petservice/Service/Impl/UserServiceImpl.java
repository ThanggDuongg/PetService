package petservice.Service.Impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import petservice.Service.UserService;
import petservice.mapping.UserMapping;
import petservice.model.Entity.RoleEntity;
import petservice.model.Entity.UserEntity;
import petservice.repository.RoleRepository;
import petservice.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    @Override
    public UserEntity saveUser(UserEntity user, String roleName) {
        Optional<RoleEntity> role = roleRepository.findByName(roleName);
        log.info("Saving user {} to database",user.getEmail());
        if(user.getRoles() == null){
            Set<RoleEntity> RoleSet = new HashSet<>();
            RoleSet.add(role.get());
            user.setRoles(RoleSet);
        }
        else{
            user.getRoles().add(role.get());
        }
        return userRepository.save(user);
    }

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        log.info("Saving Role {} to database",role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding Role {} to user {}", roleName, email);
        Optional<RoleEntity> role = roleRepository.findByName(roleName);
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if(user.get().getRoles() == null){
            Set<RoleEntity> RoleSet = new HashSet<>();
            RoleSet.add(role.get());
            user.get().setRoles(RoleSet);
        }
        else{
            user.get().getRoles().add(role.get());
        }
        userRepository.save(user.get());
    }

    @Override
    public UserEntity getUser(String email) {
        log.info("Fetching user {}",email);
        return userRepository.findByEmail(email).get();
    }

    @Override
    public List<UserEntity> getUsers() {
        log.info("Fetching all users ");
        return userRepository.findAll();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public UserEntity findByUsername(String username) {
         return userRepository.findByUserName(username).get();
    }

//    @Override
//    public UserEntity updateUserInfo(UserEntity user, InfoUserRequest userInfo) {
//        user = UserMapping.UpdateUserInfoByUser(user,userInfo);
//        return userRepository.save(user);
//    }

    @Override
    public UserEntity updateUserPassword(UserEntity user, String password) {
        user = UserMapping.UpdatePasswordByUser(user,password);
        return userRepository.save(user);
    }

    @Override
    public UserEntity deleteUser(String username) {
        UserEntity user = findByUsername(username);
        return userRepository.deleteById(user.getId()).get();
    }

    @Override
    public UserEntity updateActive(UserEntity user) {
        user.setStatus(!user.isStatus());
        return userRepository.save(user);
    }
}