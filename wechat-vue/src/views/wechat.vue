<template>
  <div class="wechat-container">
          <input type='file' id='filebutton' ref="fileInt" style='display:none' @change="fileSelected"> 
          <div class="chatwapper">
                <div class="left-tab">
                     <div class="current-user-headImg" :style="{'background-image': 'url('+require('../assets/'+cacheUserInfo.image)+')'}"></div>
                     <div class="chat-list" :class="{'chatActive': tabActive}" @click="changeToChat"></div>
                     <div class="friend-list" :class="{'friendActive': !tabActive}" @click="changeFirend"></div>
                </div>
                <div class="center-friend">
                     <!-- 聊天列表 -->
                     <div class="immediate-chat-container" v-show="tabActive">
                           <div style="position: relative;top: -30px;">当前聊天列表</div>
                           <div class="chatObj-item" v-for="(item, index) in currentChatingList" :key="index" 
                           :class="{'selectActive': chatCurrentIndex==index}"
                           @click="changeChatObject(item, index)" 
                           @contextmenu.prevent="openMenu($event,item)">
                               <div class="friend-img" :style="{'background-image': 'url('+require('../assets/'+item.image)+')'}"></div>
                               <div class="column-container">
                                 <div class="chatObj-userName">{{item.showName}}</div>
                                 <div class="chat-new-message" :title="item.messageTip">{{item.messageTip}}</div>
                                 <div class="chatObj-unReadCount" 
                                 :class="{'visibility': item.unReadCount > 0 && item.userId != chatCurrentObject.userId}">
                                 {{item.unReadCount}}
                                 </div>
                               </div>
                           </div>
                           <!-- 鼠标右键弹出操作框 -->
                           <ul v-show="visible" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
                              <li v-if="rightHandItem.isTop == false" @click="topChat">置顶聊天</li>
                              <li v-else-if="rightHandItem.isTop == true" @click="unTopChat">取消置顶</li>
                              <li>上移</li>   <!-- 暂未实现 -->
                              <li>下移</li>   <!-- 暂未实现 -->
                              <li @click="deleteOnline">删除聊天</li> 
                            </ul>
                           <!-- 鼠标右键弹出操作框 -->
                     </div>
                     <!-- 好友列表 -->
                     <div class="my-friendlist-container" v-show="!tabActive">
                           <div class="createGroup" @click="openGroupSelect">+</div>
                           <div class="friend-item" v-for="(item, index) in friendList" :key="index" 
                           :class="{'selectActive': currentSort==index}"
                           @click="clickUserInfo(item, index)">
                               <div class="friend-img" :style="{'background-image': 'url('+require('../assets/'+item.image)+')'}"></div>
                               <div class="friend-userName">{{item.showName}}</div>
                           </div>
                     </div>
                </div>
                <!-- textarea输入框区域 -->
                <div class="right-input" v-show="inputMessageActive&&!userInfoActive">
                      <div class="top-information">
                        <div class="chatObjName">{{chatCurrentObject.showName}}</div>
                      </div>
                      <div id="overflowMessage" class="message-content-container">
                           <div class="message-item" v-for="(item, index) in chatMessageList" :key="index">
                              <div class="senderImg" :class="[margintype(item.type) ? 'leftMargin-class' : 'rightMargin-class']" 
                              :style="{'background-image': 'url('+require('../assets/'+item.image)+')', 
                              'float': typeFloat(item.type)}"></div>
                              <div v-if="item.fileType==0" class="messageInfo" :class="[margintype(item.type) ? 'leftMargin-class2' : 'rightMargin-class2']" 
                              :style="{'float': typeFloat(item.type)}" v-html="renderMessage(item.message)"></div>
                              <div v-else-if="item.fileType==1" class="messageInfo" :class="[margintype(item.type) ? 'leftMargin-class2' : 'rightMargin-class2']" 
                              :style="{'float': typeFloat(item.type)}" style="display:flex">
                                     <div class="FileInformation">
                                             <div class="fileName">{{item.message}}</div>
                                             <div class="download" @click="fileDownload(item.fileName)">下载</div>
                                     </div>
                                     <div class="iconFile" :class="fileClass(item.fileName)"></div>
                              </div>
                           </div>
                      </div>
                      <div class="self-input">
                           <expression :isShowExpression="expressionActive" @seleceExpress="seleceExpress(arguments[0])"></expression>
                           <div class="emojiClass" @click="emojiClick"></div>
                           <div class="uploadClass" @click="uploadClick"></div>
                           <div class="sendMessageButton" @click="sendMessage">发送</div>
                           <textarea v-model="message" class="chat-input-textarea" @keydown="chatInput($event)">
                           </textarea>
                      </div>
                </div>
                <div class="right-input-nodata" v-show="!inputMessageActive&&!userInfoActive">

                </div>
                <!-- textarea输入框区域 -->
                <div class="right-userinfo" v-if="userInfoActive">
                       <div>{{selectFriendInfo.showName}}</div>
                       <div class="select-firend-headImg" 
                       :style="{'background-image': 'url('+require('../assets/'+selectFriendInfo.image)+')'}">
                       </div>
                       <div class="extra-info">
                          <div class="friend-property">备注：</div>
                          <div class="friend-property">地区：</div>
                          <div class="friend-property">微信号：</div>
                       </div>
                       <div class="sendToSomeBodyMessage" @click="createMessageFrame">发消息</div>
                </div>
          </div>
          <selector :isShow="isShowSelector" :friendData="friendList" @close="closeFrame" :key="selectIndex" @createChat="createChat(arguments[0])"></selector>
  </div>
</template>

<script>
import "@/css/chat.less";
import selector from "@/views/cerateMessageSelector.vue"
import expression from "@/views/expression.vue"
import * as request from "@/utils/request"
import * as constant from '@/constant/expressionData';
import { mapState } from 'vuex'
import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI);
export default {
  name: "wechat",
  components: {
     selector,
     expression,
  },
  props: {},
  data() {
    return {
      tabActive: true,
      socket: null,
      userInfoActive: false,
      currentSort: null,
      friendList: [],
      selectFriendInfo: null,  // 好友列表选中某个
      message: "",
      // 聊天列表属性
      currentChatingList: [],  //好友 聊天列表遍历
      chatCurrentIndex: null,
      chatCurrentObject: {},
      chatMessageList: [], // 聊天框的list遍历
      cacheUserInfo: null,
      //聊天userId 和聊天内容进行映射
      messageContentMapping: {},
      inputMessageActive: false,
      isShowSelector: false,
      selectIndex: 1,
      Messagetype: 1, // 1是单聊 9是群聊
      //表情弹出框显示
      expressionActive: false,
      uuid: null, //定时器Id
      //右键弹出列表属性
      visible: false,
			top: 0,
      left: 0,
      rightHandItem: {},
      //令牌 其实应该放vuex 然后用axios的拦截器统一为请求加上token  
      //这里为了能在同一个浏览器打开多个聊天窗口 token通过参数传进 不然Localstore会覆盖  F5刷新界面就会有用户覆盖问题
      token: "", 
    };
  },
  created() {
    let urlCacheParams = JSON.parse(this.urlParams);
    this.cacheUserInfo = JSON.parse(localStorage.getItem("userInfo"+urlCacheParams.userId));
    this.token = localStorage.getItem("access_token_"+urlCacheParams.userId);
    // //页面切换时清除localstore
    // window.addEventListener("beforeunload", function(e) {
    //     e.preventDefault();
    //     localStorage.removeItem("access_token_"+urlCacheParams.userId);
    //     localStorage.removeItem("userInfo"+urlCacheParams.userId);
    // });
  },
  mounted() {
    let self = this;
    $("body").click(function (e) {
    	    if (!$(e.target).closest(".self-input").length) {
    	        self.expressionActive = false;
    	    }
    });
    self.getSocket();
    // 心跳请求
    clearInterval(self.uuid);
    self.uuid = setInterval(function() {
        if (self.socket.readyState == WebSocket.OPEN) {
            let data = {
                type: 11,
                params: {},
            }
            self.socket.send(JSON.stringify(data));
        } else {
            self.$message({
              message: 'Websocket连接没有开启！',
              type: 'warning'
            });
        }  
    }, 1000 * 25)
  },
  watch: {
      chatMessageList:{
           handler(newName, oldName) {
              this.$nextTick(() => {
                  setTimeout(function() {
                    let message = document.getElementById('overflowMessage');
                    let scrollVal = message.scrollHeight;
                    $("#overflowMessage").scrollTop(scrollVal); 
               }, 10)
              })
           },
           deep: true
      },
    visible(value) {
      if (value) {
        //给页面body添加点击事件  无论点了那里都会触发
        document.body.addEventListener('click', this.closeMenu)
      } else {
        document.body.removeEventListener('click', this.closeMenu)
      }
    },
  },
  computed: {
    ...mapState({
      userInfo: "userInfo",
    }),
    // 页面传参
    urlParams: function () {
      return this.$route.query.urlParams;
    },
  },
  methods: {
    //聊天排序公用
    charSortCommon: function(flag) {      
        let sortFunction = function(a, b) {
            return a.sortIndex - b.sortIndex;
        }
        let sortFunctionDesc = function(a, b) {
            return b.sortIndex - a.sortIndex;
        }
        this.currentChatingList = this.currentChatingList.sort(flag ? sortFunction : sortFunctionDesc);
    },
    //取消置顶聊天
    unTopChat: function() {
        let self = this;
        let offset = 0;
        for (let i = 0; i < self.currentChatingList.length; i++) {
             if (self.rightHandItem.userId == self.currentChatingList[i].userId) {
                 self.currentChatingList[i].sortIndex = self.currentChatingList[i].oldIndex;
                 self.currentChatingList[i].isTop = false;
                 offset = i;
             }
        }
        if (offset > self.chatCurrentIndex) {
            self.chatCurrentIndex++;
        } else if (offset == self.chatCurrentIndex) {
            self.chatCurrentIndex = 0
        } else {
            self.chatCurrentIndex--;
        }
        self.charSortCommon(false);
    },     
    //置顶聊天
    topChat: function() {
        let self = this;
        let offset = 0;
        for (let i = 0; i < self.currentChatingList.length; i++) {
             if (self.rightHandItem.userId == self.currentChatingList[i].userId) {
                 self.currentChatingList[i].sortIndex = -1;
                 self.currentChatingList[i].isTop = true;
                 offset = i;
             }
        }
        if (offset > self.chatCurrentIndex) {
            self.chatCurrentIndex++;
        } else if (offset == self.chatCurrentIndex) {
            self.chatCurrentIndex = 0
        }
        self.charSortCommon(true);
    },
    //右键选择删除聊天
    deleteOnline: function() {
        let self = this;
        self.currentChatingList.some(function(item, i) {
             if (self.rightHandItem.userId = item.userId) {
                 self.currentChatingList.splice(i, 1);
                 self.chatCurrentIndex--;
                 self.$delete(self.messageContentMapping, self.rightHandItem.userId)
                 return true;
             }
        })
        if (self.rightHandItem.userId == self.chatCurrentObject.userId) {
            self.userInfoActive = false;
            self.inputMessageActive = false;
            self.chatCurrentIndex = null;
            self.chatCurrentObject = {};
        }
    },
    fileClass: function(fileName) {
        let suffix = fileName.split(".")[1];
        let className = "";
        switch(suffix) {
             case "txt":
                className = "icon-txt";
             break;
             case "word":
                className = "icon-word";
             break;
             case "xlsx":
                className = "icon-excel";
             break;
             case "pdf":
                className = "icon-pdf";
             break;
             case "pptx":
                className = "icon-ppt";
             break;
             default:
               break;
        }
        this.$nextTick(function(){
            let messageItemDom = $(".message-item");
            for (let j = 0; j < messageItemDom.length; j++) {
                 let temp = $(messageItemDom[j]);
                 let childrenHeight = temp.children(".messageInfo").height() + 30;
                 temp.css("height", childrenHeight + 'px');
            }
        })
        return className;
    },
    closeMenu: function() {
        this.visible = false;
    },
    // 右键弹出选择框 根据event对象拿到X Y 坐标
    openMenu: function(e, item) {
        this.rightHandItem = item; //右键显示哪个聊天item
        this.top = e.pageY;
        this.left = e.pageX;
        this.visible = true;
    },
    // v-html渲染方法
    renderMessage: function(message) {
        message = String(message);
        let lookData = constant.expresionData;
        let url = '';
        let template = '';
        for (let i = 0; i < lookData.length; i++) {
            if (message.indexOf(lookData[i].title) > -1) {
                url = require("../assets/expression/" + lookData[i].name);
                template = '<img class="messageImg" src='+url+'>';
                message = message.replace(lookData[i].title, template);
            }
        }
        this.$nextTick(function(){
            let messageItemDom = $(".message-item");
            for (let j = 0; j < messageItemDom.length; j++) {
                 let temp = $(messageItemDom[j]);
                 let childrenHeight = temp.children(".messageInfo").height() + 30;
                 temp.css("height", childrenHeight + 'px');
            }
        })
        return message;
    },
    //表情选择后
    seleceExpress: function(val) {
        this.expressionActive = false;
        this.message += val.title;
    },
    // 弹出表情选择框
    emojiClick: function() {
        this.expressionActive = !this.expressionActive;
    },
    fileDownload: function(fileName) {
        let params = {
            fileName:  fileName,
        }
        request.postUrl("/wechat/download", params, this.token).then(function(res){
            let url = window.URL.createObjectURL(new Blob([res.data]));
            let link = document.createElement('a');
            link.style.display = 'none';
            link.href = url;
            //文件名
            link.setAttribute('download', res.config.params.fileName);
            document.body.appendChild(link);
            link.click();
        })
    },
    fileSelected: function() {
        let self = this;
        let cacheUserInfo = self.cacheUserInfo;
        let file = this.$refs.fileInt.files[0];
        let fd = new FormData();
        fd.append("file", file);
        let fileDom = document.getElementById('filebutton');
        fileDom.value = ''; //file的value值只能设置为空字符串
        request.postFile("/wechat/upload", fd, self.token).then(function(res){
          if(res.data.obj) {
            let FileResultObj = res.data.obj;
            let parmas = {
                message: FileResultObj.fileName + "(" + FileResultObj.fileSize +"B)",
                fileName: FileResultObj.fileName,
                type: self.Messagetype,
                image: cacheUserInfo.image,
                fileType: 1
            }
            self.chatMessageList.push(parmas);
            self.messageContentMapping[self.chatCurrentObject.userId] = request.copy(self.chatMessageList);
            if (self.socket.readyState == WebSocket.OPEN) {
                let data = {
                    type: self.Messagetype,
                    params: {
                      toMessageId: self.chatCurrentObject.userId,
                      message: parmas.message,
                      fileType: 1,
                    },
                }
                self.socket.send(JSON.stringify(data));
            } else {
                self.$message({
                  message: 'Websocket连接没有开启！',
                  type: 'warning'
                });
            }            
          }
        })
    },
    uploadClick: function() {
       $("#filebutton").click();
    },
    commonCode: function() {
        let self = this;
        self.isShowSelector = false; //隐藏弹出选择框
        self.tabActive = true;
        self.userInfoActive = false;
        self.inputMessageActive = true;
    },
    // 通过选择框确定创建的聊天  可能是单聊  可能是群聊
    createChat: function(userList) {
        let self = this;
        if (userList.length == 1) {
            // 转化为创建单聊
            if (self.currentChatingList.length == 0) {
                self.chatCurrentIndex = 0;
            }
            for (let i = 0; i < self.currentChatingList.length; i++) {
                if (userList[0].userId == self.currentChatingList[i].userId) {
                    self.chatCurrentIndex = i;
                    self.chatCurrentObject = self.currentChatingList[i];
                    self.chatMessageList = self.messageContentMapping[self.chatCurrentObject.userId] || [];
                    self.commonCode();
                    return;
                }
            }
            let createParams = {
                messageTip: "",
                unReadCount: 0,
                sortIndex: self.currentChatingList.length,
                oldIndex: self.currentChatingList.length,
                isTop: false,
            };
            $.extend(createParams, userList[0]);
            self.chatCurrentObject = createParams;
            // 给列表添加到头部
            self.currentChatingList.unshift(JSON.parse(JSON.stringify(createParams)));
            self.chatMessageList = self.messageContentMapping[createParams.userId] || [];
            self.commonCode();
            return;
        }
        let userIdList = [];
        for (let i = 0; i < userList.length; i++) {
            userIdList.push(userList[i].userId);
        }
        if (self.socket.readyState == WebSocket.OPEN) {
              let data = {
                  type: 3,
                  params: {
                    userIdList: userIdList.toString(),
                  },
              }
              self.socket.send(JSON.stringify(data));
        } else {
              self.$message({
                message: 'Websocket连接没有开启！',
                type: 'warning'
              });
        }        
    },
    // 关闭群聊选择框
    closeFrame: function(val) {
       this.friendList.forEach(function(item){
          item.selected = false;
       })
       this.isShowSelector = val;
    },
    // 弹出群聊选择框
    openGroupSelect: function() {
      this.selectIndex++;
      this.isShowSelector = true;
    },
    margintype: function(val) {
        if (val == 1 || val == 9) {
           return false;
        } else {
           return true;
        }
    },
    typeFloat: function(type) {
       if (type == 1 || type == 9) {
          return 'right';
       } else {
         return 'left';
       }
    },
    changeChatObject: function(val, index) {
       if (val.hasOwnProperty("groupId")) {
          this.Messagetype = 9;
       } else {
          this.Messagetype = 1;
       }
       this.chatCurrentIndex = index;
       this.inputMessageActive = true;  // 展示输入框
       val.unReadCount = 0; // 打开聊天消息后  未读置为0
       this.chatCurrentObject = $.extend(true, {messageTip: ""}, val);
       //聊天内容框 需要根据不同的聊天对象映射不同的内容框
       this.chatMessageList = this.messageContentMapping[val.userId] || [];
    },
    createMessageFrame: function(val) {
       if (this.currentChatingList.length == 0) {
          this.chatCurrentIndex = 0;
       }
       for (let i = 0; i < this.currentChatingList.length; i++) {
           if (this.selectFriendInfo.userId == this.currentChatingList[i].userId) {
                this.chatCurrentIndex = i;
                this.chatCurrentObject = this.currentChatingList[i];
                this.chatMessageList = this.messageContentMapping[this.chatCurrentObject.userId] || [];
                this.tabActive = true;
                this.userInfoActive = false;
                this.inputMessageActive = true;
                return;
           }
       }
       let createParams = {
           messageTip: "",
           unReadCount: 0,
           sortIndex: this.currentChatingList.length,
           oldIndex: this.currentChatingList.length,
           isTop: false,
       };
       $.extend(createParams, this.selectFriendInfo);
       this.chatCurrentObject = createParams;
       // 给列表添加到头部
       this.currentChatingList.unshift(JSON.parse(JSON.stringify(createParams)));
       this.chatMessageList = this.messageContentMapping[createParams.userId] || [];
       this.tabActive = true;
       this.userInfoActive = false;
       this.inputMessageActive = true;
    },
    //点击发送消息
    sendMessage: function() {
       let self = this;
       if (self.message == "") {
            self.$message({
              message: '发送内容不能为空！',
              type: 'warning'
            });
           return;
       }
       let cacheUserInfo = self.cacheUserInfo;
       let parmas = {
           message: self.message,
           type: self.Messagetype,
           image: cacheUserInfo.image,
           fileType: 0,
       }
       self.chatMessageList.push(parmas);
       self.messageContentMapping[self.chatCurrentObject.userId] = request.copy(self.chatMessageList);
       self.message = "";
      if (self.socket.readyState == WebSocket.OPEN) {
            let data = {
                type: self.Messagetype,
                params: {
                  toMessageId: self.chatCurrentObject.userId,
                  message: parmas.message,
                  fileType: 0,
                },
            }
            self.socket.send(JSON.stringify(data));
       } else {
            self.$message({
              message: 'Websocket连接没有开启！',
              type: 'warning'
            });
       }
     },
    chatInput: function(event) {
      if (event.keyCode == 13) {
         console.log("触发enter输入事件");
         if (this.message.trim() == "") {
            alert("不能发送空消息")
         }
      }
    },
    clickUserInfo: function(val, index) {
        let self = this;
        self.userInfoActive = true;
        self.currentSort = index; 
        // 当前点击了那个好友详细信息
        self.selectFriendInfo = val;
    },
    changeToChat: function() {
        this.tabActive = true;
        this.userInfoActive = false;
        this.inputMessageActive = true;
    },
    /**
     * 切换到好友列表
     */
    changeFirend: function() {
        let self = this;
        self.tabActive = false;
        self.inputMessageActive = false;
        if (self.selectFriendInfo) {
           self.userInfoActive = true;
        }
        // f5刷新界面后，vuex会重新更新state 所以存储的数据会丢失
        let cacheUserInfo = self.cacheUserInfo;
        let params = {
           userId: self.userInfo.userId || cacheUserInfo.userId
        }
        request.getJSON("/wechat/friendList", params, self.token).then(function(res){
          self.friendList = res.data.obj;
          for (let i = 0; i < self.friendList.length; i++) {
            self.friendList[i].selected = false; // 供给弹出框chebox初始化选择
          }
        }).catch(function(error){
            if (error.response.data.error == "invalid_token") {
                self.$message({
                  message: 'token失效请重新登录！',
                  type: 'warning'
                });
                 self.$router.push('/')
            }
        })
    },
    getSocket: function () {
      let socket;
      let self = this;
      if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket;
      }
      if (window.WebSocket) {
        socket = new WebSocket("ws://localhost:9999");
        socket.onmessage = function (event) {
          let json = JSON.parse(event.data);
          if (json.status == 200) {
            switch(json.type) {
               case 2:
                 self.handleSingleMessage(json);
                 break;
               case 4:
                 self.handleCreateChatResponse(json);
                 break;
               case 10: //群聊消息接受 其实 也是适配单聊
                 self.handleGroupMessageResponse(json);
                 break;  
               case 12:
                 console.log("收到心跳检测回复")
                 break;  
                 default:
                 break;              
            }
          } else {
              self.$message({
                message: '发送失败 对方不在线',
                type: 'warning'
              });
          }
        };
        // socket连接 连接成功1秒后，将用户信息注册到服务器在线用户表
        socket.onopen = setTimeout(function (event) {
            console.log("WebSocket已成功连接！");
            if (!window.WebSocket) {
                return;
            }
            let cacheUserInfo = self.cacheUserInfo;
            if (socket.readyState == WebSocket.OPEN) {
                let data = {
                    type: 7,
                    params: cacheUserInfo,
                }
                socket.send(JSON.stringify(data));
            } else {
                self.$message({
                  message: 'Websocket连接没有开启！',
                  type: 'warning'
                });
            }
        }, 1000);
        //socket关闭
        socket.onclose = function (event) {
            clearInterval(self.uuid);
        };
      } else {
        self.$message({
          message: '您的浏览器不支持WebSocket！',
          type: 'warning'
        });
      }
      this.socket = socket;
    },
    // 处理收到单聊的信息
    handleSingleMessage: function(jsonObj) {
       let self = this;
       let parmas = {
           message: jsonObj.params.message,
           type: 2,
           image: jsonObj.params.fromUser.image,
           fileType: jsonObj.params.fileType,
       }
       if (jsonObj.params.fileType == "1") {
           parmas.fileName = jsonObj.params.message.split("(")[0];
       }
       let fromUser = jsonObj.params.fromUser;
       let toUser = jsonObj.params.toUser;
       let exist = false;
       for (let i = 0; i < self.currentChatingList.length; i++) {
           if (fromUser.userId == self.currentChatingList[i].userId) {
              self.currentChatingList[i].messageTip = parmas.message;
              if (self.chatCurrentObject.userId != fromUser.userId) {
                  self.currentChatingList[i].unReadCount++;
              }
              exist = true;
           }
       }
       if (!exist) {
          //聊天列表不存在  就添加新记录   并且接收到一条消息那么未读提醒unReadCount开始就为0
          let createParams = {
            messageTip: parmas.message,
            unReadCount: 1,
            sortIndex: self.currentChatingList.length,
            oldIndex: self.currentChatingList.length,
            isTop: false,
          };
          $.extend(createParams, fromUser);
          if (self.currentChatingList.length > 0 && JSON.stringify(self.chatCurrentObject) != "{}") {
              self.chatCurrentIndex++;
          }
          self.currentChatingList.unshift(JSON.parse(JSON.stringify(createParams)));
       }
       if (self.messageContentMapping[fromUser.userId] == undefined) {
          self.messageContentMapping[fromUser.userId] = [];
       }
       self.messageContentMapping[fromUser.userId].push(parmas);
       if (JSON.stringify(self.chatCurrentObject) != "{}" && self.chatCurrentObject.userId == fromUser.userId) {
          self.chatMessageList = self.messageContentMapping[self.chatCurrentObject.userId];
       }
       //self.chatMessageList = self.messageContentMapping[fromUser.userId];
       //self.messageContentMapping[fromUser.userId] = request.copy(self.chatMessageList);
    },
    // 处理创建聊天成功返回处理
    handleCreateChatResponse: function(jsonObj) {
        let self = this;
        let createParams = {
           messageTip: "",
           unReadCount: 0,
           userId: jsonObj.groupId,
           showName: jsonObj.nameList.toString(),
           image: 'group.jpg',
           groupId: jsonObj.groupId,
           sortIndex: self.currentChatingList.length,
           oldIndex: self.currentChatingList.length,
           isTop: false,
        };
        if (self.currentChatingList.length > 0 && JSON.stringify(self.chatCurrentObject) != "{}") {
           self.chatCurrentIndex++;
        }
        self.currentChatingList.unshift(JSON.parse(JSON.stringify(createParams)));
        self.commonCode();
    },
    // 接受群聊信息返回处理
    handleGroupMessageResponse: function(jsonObj) {
        let self = this;
        let parmas = {
            message: jsonObj.params.message,
            type: 2,
            image: jsonObj.params.fromUser.image,
            fileType: jsonObj.params.fileType,
            showName: jsonObj.params.nameList.toString(),
        }
        if (jsonObj.params.fileType == "1") {
            parmas.fileName = jsonObj.params.message.split("(")[0];
        }
        let fromUser = jsonObj.params.fromUser;
        let groupId = jsonObj.params.groupId;
        let exist = false;
        for (let i = 0; i < self.currentChatingList.length; i++) {
            if (groupId == self.currentChatingList[i].groupId) {
                self.currentChatingList[i].messageTip = parmas.message;
                if (self.chatCurrentObject.userId != groupId) {
                    self.currentChatingList[i].unReadCount++;
                }
                exist = true;
            }
        }
        if (!exist) {
          //聊天列表不存在  就添加新记录   并且接收到一条消息那么未读提醒unReadCount开始就为0
          let createParams = {
            messageTip: parmas.message,
            unReadCount: 1,
            userId: groupId,
            showName: parmas.showName,
            image: 'group.jpg',
            groupId: groupId,
            sortIndex: self.currentChatingList.length,
            oldIndex: self.currentChatingList.length,
            isTop: false,
          };
          if (self.currentChatingList.length > 0 && JSON.stringify(self.chatCurrentObject) != "{}") {
              self.chatCurrentIndex++;
          }
          self.currentChatingList.unshift(JSON.parse(JSON.stringify(createParams)));
        }
        if (self.messageContentMapping[groupId] == undefined) {
            self.messageContentMapping[groupId] = [];
        }
        self.messageContentMapping[groupId].push(parmas);
        if (JSON.stringify(self.chatCurrentObject) != "{}" && self.chatCurrentObject.userId == groupId) {
           self.chatMessageList = self.messageContentMapping[groupId];
        }    
    },
  },
};
</script>