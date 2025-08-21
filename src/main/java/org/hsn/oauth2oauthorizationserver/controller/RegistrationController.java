package org.hsn.oauth2oauthorizationserver.controller;


import lombok.RequiredArgsConstructor;
import org.hsn.oauth2oauthorizationserver.requests.ClientRegistrationRequest;
import org.hsn.oauth2oauthorizationserver.requests.ResourceOwnerRegistrationRequest;
import org.hsn.oauth2oauthorizationserver.service.client.ClientService;
import org.hsn.oauth2oauthorizationserver.service.resourceowner.ResourceOwnerService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
private  final ResourceOwnerService resourceOwnerService;
private  final ClientService clientService;

    @PostMapping("/user")
    public  void addUsers(@RequestBody ResourceOwnerRegistrationRequest request){
        resourceOwnerService.createResourceOwner(request);
    }
    @PostMapping("/client")
    public  void  registerClient(@RequestBody ClientRegistrationRequest request){
        clientService.registerClient(request);
    }

}
