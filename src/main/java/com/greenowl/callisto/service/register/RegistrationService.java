package com.greenowl.callisto.service.register;

import com.greenowl.callisto.domain.Authority;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.factory.UserFactory;
import com.greenowl.callisto.repository.AuthorityRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.service.MailService;
import com.greenowl.callisto.service.util.UserUtil;
import com.greenowl.callisto.web.rest.dto.UserDTO;
import com.greenowl.callisto.web.rest.dto.user.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.greenowl.callisto.security.AuthoritiesConstants.USER;

@Service
public class RegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private MailService mailService;

    public UserDTO register(CreateUserRequest req) {
        return createUserInformation(req.getLogin(), req.getFirstName(), req.getLastName(),
                req.getRegion(), req.getPassword());
    }

    /**
     * Create a new user and persist that user into the data store.
     *
     * @param login
     * @param firstName
     * @param lastName
     * @return newUser
     */
    private UserDTO createUserInformation(String login, String firstName, String lastName,
                                          String region, String desiredPassword) {
        Authority authority = authorityRepository.findOne(USER);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        String encryptedPassword = passwordEncoder.encode(desiredPassword);
        User newUser = UserFactory.create(login, firstName, lastName, region, encryptedPassword, authorities);
        LOG.debug("Created Information for User: {}", newUser);
        UserDTO dto = UserUtil.getUserDTO(newUser);
        LOG.info("Returning newly created user {}", dto);
        return dto;
    }


}