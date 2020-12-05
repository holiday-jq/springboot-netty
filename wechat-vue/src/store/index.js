import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
      userInfo: {}
  },
  mutations: {
     commitInfo: function(state, info) {
      state.userInfo = info;
      localStorage.setItem("userInfo", JSON.stringify(info));
    }
  },
  actions: {
     saveUserInfo: function(state, info) {
       this.commit("commitInfo", info)
     }
  },
  modules: {
  }
})
