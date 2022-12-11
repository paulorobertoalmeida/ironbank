package com.ihbank.ihbank.services.user;

import com.ihbank.ihbank.dto.ThirdPartyDTO;
import com.ihbank.ihbank.models.users.Role;
import com.ihbank.ihbank.models.users.ThirdParty;
import com.ihbank.ihbank.repositories.users.RoleRepository;
import com.ihbank.ihbank.repositories.users.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ThirdParty createThirdParty(ThirdPartyDTO thirdPartyDTO) {
        ThirdParty newThirdParty = new ThirdParty(thirdPartyDTO.getUsername(), passwordEncoder.encode(thirdPartyDTO.getPassword()), thirdPartyDTO.getName(), thirdPartyDTO.getHashedKey());
        thirdPartyRepository.save(newThirdParty);
        Role role = new Role("THIRD PARTY", newThirdParty);
        roleRepository.save(role);
        return newThirdParty;
    }
}
