package com.web.service.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.google.api.services.people.v1.model.Name;
import com.web.service.services.GooglePeopleAPIService;

@Controller
public class LoginController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

  private static String authorizationRequestBaseUri = "oauth2/authorization";
  Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

  @Autowired
  private ClientRegistrationRepository clientRegistrationRepository;

  @Autowired
  private OAuth2AuthorizedClientService authorizedClientService;

  @Autowired
  private GooglePeopleAPIService peopleAPIService;

  @GetMapping("/oauth_login")
  public String getLoginPage(Model model) {
    Iterable<ClientRegistration> clientRegistrations = null;

    ResolvableType type =
        ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);

    if (type != ResolvableType.NONE
        && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
      clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
    }

    clientRegistrations
        .forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
            authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
    model.addAttribute("urls", oauth2AuthenticationUrls);

    return "oauth_login";
  }

  @GetMapping("/loginSuccess")
  public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
    OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
        authentication.getAuthorizedClientRegistrationId(), authentication.getName());

    String userInfoEndpointUri =
        client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

    // if (!StringUtils.isEmpty(userInfoEndpointUri)) {
    // LOGGER.info("User Info Endpoint URI: {}", userInfoEndpointUri);
    // RestTemplate restTemplate = new RestTemplate();
    // HttpHeaders headers = new HttpHeaders();
    // headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
    // HttpEntity entity = new HttpEntity("", headers);
    // ResponseEntity<Map> response =
    // restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
    // Map userAttributes = response.getBody();
    // model.addAttribute("name", userAttributes.get("name"));
    // }

    if (StringUtils.isNotBlank(userInfoEndpointUri)) {
      List<Name> names = peopleAPIService.getPersonNames(userInfoEndpointUri);
      model.addAttribute("name", names.get(0).getDisplayName());
    }

    return "loginSuccess";
  }

  @GetMapping("/loginFailure")
  public String loginFailure(Model model) {
    return "loginFailure";
  }

}
