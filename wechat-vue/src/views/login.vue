<template>
     <div class="login-container">
             <div class="input-container">
                 <div class="top-information">欢迎您，请登录</div>
                 <div class="input-warpper">
                 <span></span><input class="input-content" placeholder="用户名" type="input" autocomplete="off" v-model="userName">
                 </div>
                 <div class="input-warpper">
                 <span></span><input class="input-content" placeholder="密码" type="password" autocomplete="off" v-model="password">
                 </div>
                 <button class="commit" @click="toLogin">登录</button>
                 <div class="foot-tip">如果忘记密码，请联系管理员重置。</div>
             </div>
     </div>
</template>

<script>
import  "@/css/login.less"
import * as request from "@/utils/request";
import { mapActions } from 'vuex';
export default {
    data() {
        return {
            userName: "",
            password: "",
        }
    },
    created() {
        
    },
    mounted() {
        
    },
    methods: {
        ...mapActions({
            saveUserInfo: 'saveUserInfo'
        }),
        toLogin: function() {
            let me = this;
            let loginParam = {
                username: me.userName,
                password: me.password,
                grant_type: "password",
                scope: "app",
                client_id: "client",
                client_secret: "secret"
            }
            request.postUrlContentType("/oauth/token", loginParam).then(function(res) {
                if (res.data) {
                   //再去请求userId
                   let userParams = {
                        userName: me.userName,
                   }
                   let token = res.data.access_token;
                   request.postJSON("/wechat/login/getUser", userParams, token).then(function(res) {
                        me.saveUserInfo(res.data.obj);
                        let params = {
                            userId: res.data.obj.userId
                        }
                        localStorage.setItem("access_token_"+params.userId, token)
                        let encodeUrl = window.encodeURI(JSON.stringify(params));
                        me.$router.push('/wechat?urlParams='+encodeUrl)
                   })
                } else {
                    alert("登录失败，密码错误！")
                }
            })
        }
    },
}
</script>

<style lang="css">

</style>