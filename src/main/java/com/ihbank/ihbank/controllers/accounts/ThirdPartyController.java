package com.ihbank.ihbank.controllers.accounts;

import com.ihbank.ihbank.dto.ThirdPartyDTO;
import com.ihbank.ihbank.models.users.ThirdParty;
import com.ihbank.ihbank.services.user.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;


    @PostMapping("/create-third-party")  // solo admin -- OK
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody ThirdPartyDTO thirdPartyDTO) {
        return thirdPartyService.createThirdParty(thirdPartyDTO);
    }
}
