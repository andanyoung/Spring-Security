package cn.andyoung.ssvalidatecode.security.validate.smscode;

import cn.andyoung.ssvalidatecode.security.UserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

// DaoAuthenticationProvider
public class SmsAuthenticationProvider implements AuthenticationProvider {

  private UserDetailService userDetailService;
  /**
   * 具体的身份认证逻辑
   *
   * @param authentication
   * @return
   * @throws AuthenticationException
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;

    UserDetails userDetails =
        userDetailService.loadUserByUsername((String) authenticationToken.getPrincipal());

    if (userDetails == null) throw new InternalAuthenticationServiceException("未找到与该手机号对应的用户");

    SmsAuthenticationToken authenticationResult =
        new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());

    authenticationResult.setDetails(authenticationToken.getDetails());

    return authenticationResult;
  }

  // **指定了支持处理的Token类型为SmsAuthenticationToken，
  @Override
  public boolean supports(Class<?> authentication) {
    return SmsAuthenticationToken.class.isAssignableFrom(authentication);
  }

  public UserDetailService getUserDetailService() {
    return userDetailService;
  }

  public void setUserDetailService(UserDetailService userDetailService) {
    this.userDetailService = userDetailService;
  }
}
