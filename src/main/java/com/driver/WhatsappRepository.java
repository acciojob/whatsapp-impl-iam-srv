package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    private HashMap<String , User> UserMap;
    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;


    }

   public boolean newUser(String phnNo){
        if(UserMap.containsKey(phnNo))return false;
         return true;
   }
   public void createUser(String name , String phoneNumber ){

        UserMap.put(phoneNumber, new User(name,phoneNumber) );
   }



    public Group createGroup(List<User> users){

        if(users.size()==2) return this.createPersonalChat(users);

        customGroupCount++;
        String groupName = "Group "+this.customGroupCount;

        Group group  = new Group(groupName, users.size());
        groupUserMap.put(group, users);
        adminMap.put(group,users.get(0));
        return group;
    }

   public Group createPersonalChat(List<User> users){

        String groupName = users.get(1).getName();
        Group personalGroup = new Group(groupName , 2);
        groupUserMap.put(personalGroup,users );
        return personalGroup;
   }


    public int createMessage(String content){
        messageId++;
        Message msg  = new Message(messageId , content , new Date());
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{  // send message
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if(!this.userExistsInGroup(group, sender)) throw new Exception("You are not allowed to send message"); // checking is the user is in the group

        // getting the message from groupMessage
        List<Message> messages = new ArrayList<>();
        if(groupMessageMap.containsKey(group)) messages = groupMessageMap.get(group);
        messages.add(message);
        groupMessageMap.put(group, messages);
        return messages.size();
    }

    public boolean userExistsInGroup(Group group, User sender) {
        List<User> users = groupUserMap.get(group);
        for(User user: users) {
            if(user.equals(sender)) return true;
        }

        return false;
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
      if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
      if(!adminMap.get(group).equals(approver)) throw new Exception("Approver does not have rights");
      if(!this.userExistsInGroup(group,user)) throw new Exception("User is not a participant");

      adminMap.put(group,user);
      return "SUCCESS";
    }

//    public int removeUser(User user) throws Exception{
//        //This is a bonus problem and does not contains any marks
//        //A user belongs to exact(ly one group
//        //If user is not found in any group, throw "User not found" exception
//        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
//        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
//        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)
//
//        for(Group g : groupUserMap.keySet()){
//           if(this.userExistsInGroup())
//        }
//    }
}
