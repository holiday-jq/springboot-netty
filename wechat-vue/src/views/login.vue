<template>
     <div class="login-container">
             <div class="input-container">
                 <div class="top-information">请登录</div>
                 <div class="input-warpper" style="margin-right: 17px;">
                 <span>用户名：</span><input class="input-content" type="input" v-model="userName">
                 </div>
                 <div class="input-warpper">
                 <span>密码：</span><input class="input-content" type="password" v-model="password">
                 </div>
                 <button class="commit" @click="toLogin">登录</button>
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
                userName: me.userName,
                password: me.password,
            }
            request.postJSON("/wechat/login", loginParam).then(function(res) {
                console.log(res);
                if (res.data.obj) {
                   me.saveUserInfo(res.data.obj);
                   me.$router.push('/wechat')
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