package org.hsn.oauth2oauthorizationserver.controller;
import lombok.RequiredArgsConstructor;
import org.hsn.oauth2oauthorizationserver.requests.ClientRegistrationRequest;
import org.hsn.oauth2oauthorizationserver.requests.ResourceOwnerRegistrationRequest;
import org.hsn.oauth2oauthorizationserver.service.client.ClientService;
import org.hsn.oauth2oauthorizationserver.service.resourceowner.ResourceOwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
private  final ResourceOwnerService resourceOwnerService;
private  final ClientService clientService;

    @PostMapping("/user")
    public ResponseEntity <Void >addUsers(@RequestBody ResourceOwnerRegistrationRequest request){
        resourceOwnerService.createResourceOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/client")
    public  ResponseEntity <Void>  registerClient(@RequestBody ClientRegistrationRequest request){
        clientService.registerClient(request);
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
