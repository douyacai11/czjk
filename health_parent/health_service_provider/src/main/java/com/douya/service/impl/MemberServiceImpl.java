package com.douya.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.douya.dao.MemberDao;
import com.douya.pojo.Member;
import com.douya.service.MemberService;
import com.douya.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员服务
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //保存会员信息
    public void add(Member member) {
        String password = member.getPassword();
        if(password != null){
            //使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> memberCount = new ArrayList<Integer>();
        for(String month : months){
            String data =month+".31"; //格式：2019.04.31
            Integer count = memberDao.findMemberCountBeforeDate(data);
            memberCount.add(count);
        }
        return memberCount;
    }

}
