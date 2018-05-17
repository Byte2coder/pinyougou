package com.pinyougou.shop.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import com.pinyougou.sellergoods.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserDeatailsServiceImpl implements UserDetailsService{

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("从页面传过来的值:"+username);
       /* List<GrantedAuthority> list=new ArrayList<>();
        list.add(new SimpleGrantedAuthority("USER_SELLER"));*/

       //从该数据库将用户数据查询出来-匹配
        TbSeller tbSeller = sellerService.findOne(username);

        //1.根据传递过来的用户名从数据库查询该用户名对应的对象,如果对象不存在,返回null;
          if (tbSeller==null){
              return null;
          }
      //  System.out.println("说明用户存在:"+tbSeller.getName());

        //2.对象存在,验证是否被审核 0,未被审核,1,被审核
        if (!"1".equals(tbSeller.getStatus())){
            return null;
        }

        //匹配 密码是和前台传递的一致,(spring security自己实现)
       // System.out.println("from database:"+tbSeller.getPassword());


        return new User(username,tbSeller.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_SELLER"));
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println("first encoding:"+encode);
        String encode2 = bCryptPasswordEncoder.encode("123456");
        System.out.println("first encoding:"+encode2);

    }
}
