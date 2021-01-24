1:前言                                                                                                                                                                   
   最近在学习网络知识，对于java开发来说，Netty是一个非常重要的框架，无论是为了面试还是日常工作中，如Dubbo底层其实是用了Netty,
   又或者我们的聊天通信功能，都可能有Netty的身影，作为java开发者 我们都应该掌握它。此项目是一个前后端分离的、以Netty为核心，以Websocket为通信协议的聊天系统，整体功能仿照电脑版微信，
   但是电脑版微信功能非常多，因此只实现部分功能，如：用户登录、查看我的好友列表、单聊、创建群聊、群消息发送、表情功能发送、文件发送、心跳和空闲检测。

2：技术路线                                                                                                                                                              
前端： Webpack + vue + less + element ui                                                                                                                                    
后端：springboot框架 + Netty网络编程框架 + jdbcTemplate                                                                                                                               
数据库： mysql                                                                                                                                                
协议： http + websocket

3:环境搭建                                                                                                                                                     
前端：
运行环境搭建：首先要安装Node.js  安装链接https://nodejs.org/zh-cn/                                                                                           
然后运行cmd进入控制台   验证是否安装成功：  node -v  和 npm -v  如果能显示版本号出来说明Node.js安装成功
用VsCode(或看个人喜欢用什么编辑器)，到项目根目录，首先执行npm install安装依赖，然后npm run serve启动一个node服务就可以访问前端资源                                                     

后端：
安装好maven，并在开发编辑器（如eclipse）中配置好maven，然后启动springboot工程

项目详情：https://blog.csdn.net/qq_38355969/article/details/112099760

备注：用户登录密码123456，数据库存的是加密后的
