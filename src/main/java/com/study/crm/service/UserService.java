package com.study.crm.service;

import com.study.crm.base.BaseService;
import com.study.crm.dao.UserMapper;
import com.study.crm.dao.UserRoleMapper;
import com.study.crm.model.UserModel;
import com.study.crm.utils.AssertUtil;
import com.study.crm.utils.Md5Util;
import com.study.crm.utils.PhoneUtil;
import com.study.crm.utils.UserIDBase64;
import com.study.crm.vo.User;
import com.study.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public  class UserService extends BaseService<User,Integer>  {
    @Resource
    @Qualifier("com.study.crm.dao.UserMapper")
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /***
     * login成功后返回一个userModel
     * 1.检查用户、密码是否为空
     * 2.通过用户名查询user
     * 3.检查密码是否正确
     * 4.生成一个usermodel返回给controller
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        checkLoginParams(userName,userPwd);
        User user=userMapper.queryByUserByName(userName);
        AssertUtil.isTrue(user==null,"用户姓名不存在");
        checkUserPwd(userPwd,user.getUserPassword());
        return buildUserInfo(user);
    }

    private UserModel buildUserInfo(User user) {
        UserModel userModel=new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return  userModel;
    }

    private void checkUserPwd(String userPwd, String userPassword) {
        userPwd= Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(userPassword),"用户密码不正确");
    }

    /***
     * 检查用户名、密码是否为空
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户姓名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }

    /***
     *  通过id查询用户user
     * ​参数校验：
     * ​           user是否为空
     * ​           旧密码是否为空，旧密码是否错误
     * ​          新密码是否为空，新密码是否与旧密码相同
     * ​          确认新密码是否为空，确认信密码是否与新密码相同
     * ​ 对新密码进行加密
     * ​ 调用dao层进行密码更新
     *   执行更新判断受影响的行数用于判断是否更新成功
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId,String oldPwd,String newPwd,String repeatPwd ){
        User user=userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(user==null,"待更新记录不存在");
        System.out.println(oldPwd);
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
        user.setUserPassword(Md5Util.encode(newPwd));
        userMapper.updateByPrimaryKeySelective(user);
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败");

    }

    private void checkPasswordParams(User user,String oldPwd, String newPwd, String repeatPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"旧密码不能为空");
        AssertUtil.isTrue(user.getUserPassword().equals(Md5Util.encode(oldPwd)),"原始密码不正确");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        AssertUtil.isTrue(newPwd.equals(oldPwd),"新密码不能与旧密码相同");
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认新密码不能为空");
        AssertUtil.isTrue(!repeatPwd.equals(newPwd),"确认新密码与新密码不一致");
    }

    /***
     * 添加用户：
     *    1. 参数校验（主要是判空之类的操作）
     *        UserName   也要唯一
     *        email
     *        phone
     *    2. 设置默认值（主要是更新时间等）
     *       isValid
     *       createDate
     *       updateDate
     *       默认password 123456（需要加密）
     *    3. 调用dao层方法进行插入
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),null);
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPassword(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user)!=1,"添加用户失败");
        //用户角色关联
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /***
     * 用户角色关联
     * 主要是更新t_user_role表
     * @param userId
     * @param roleIds
     */
    private void relationUserRole(Integer userId, String roleIds) {
        Integer count=userRoleMapper.countUserRoleByUserId(userId);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色分配失败");
        }
        if(StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoleList=new ArrayList<>();
            String[] roleIdsArray=roleIds.split(",");
            for (String roleId : roleIdsArray) {
                UserRole userRole=new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList)!=userRoleList.size(),"用户角色分配失败");
        }
    }

    private void checkUserParams(String userName, String email, String phone,Integer userId) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //需要验证用户名的唯一性
        User temp=userMapper.queryByUserByName(userName);
        //如果是更新操作，当用用户名查询的时候是可以存在的
        AssertUtil.isTrue(temp !=null && !(temp.getId().equals(userId)),"用户名已存在，请重新输入");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"电话号码不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"电话号码格式不正确");
    }

    /***
     * 更新用户信息
     * 与添加操作一致
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        AssertUtil.isTrue(user.getId()==null,"待更新记录不存在");
        User temp=userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp==null ,"待更新记录不存在");
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),user.getId());
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)!=1,"更新用户失败");
        relationUserRole(user.getId(),user.getRoleIds());
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids){
        AssertUtil.isTrue(null==ids || ids.length<1,"待删除记录不存在");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!=ids.length,"用户数据删除失败");
        for(Integer userId:ids){
            Integer count=userRoleMapper.countUserRoleByUserId(userId);
            if(count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"删除用户失败！");
            }
        }
    }

    public List<Map<String, Object>> queryAllCustomerManager() {
        return userMapper.queryAllCustomerManager();
    }
}
