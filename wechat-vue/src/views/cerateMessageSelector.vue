<template>
  <div class="selector-container" v-show="isShow">
    <div class="left-container">
      <input class="searchInput" type="input" placeholder="搜索(暂不提供)" />
      <div
        class="friend-item"
        v-for="(item, index) in friendData"
        :key="index"
        :class="{ selectActive: currentSort == index }"
        @click="clickUserInfo(item, index)"
      >
        <div
          class="friend-img"
          :style="{
            'background-image':
              'url(' + require('../assets/' + item.image) + ')',
          }"
        ></div>
        <div class="friend-userName">{{item.showName}}</div>
        <div class="checkbox-container"><input class="tui-checkbox" type="checkbox" v-model="item.selected" @change="selectChange"></div>
      </div>
    </div>
    <div class="right-container">
      <div class="close" @click="close">✖</div>
      <div v-if = "SelectedFriendList.length == 0" class="top-information">请勾选需要添加的联系人</div>
      <div v-else class="top-information">已选择了{{SelectedFriendList.length}}个联系人</div>
       <div
        class="friend-item"
        v-for="(item, index) in SelectedFriendList" :key="index"
        @click="clickUserInfo(item, index)">
        <div
          class="friend-img"
          :style="{
            'background-image':
              'url(' + require('../assets/' + item.image) + ')',
          }"
        ></div>
        <div class="friend-userName">{{item.showName}}</div>
        <div class="unselect" @click="unSelected(item)">✖</div>
      </div>

      <div class="footer-button">
          <div class="none-makesure" v-if="SelectedFriendList.length == 0">确定</div> 
          <div v-else class="makesure" @click="makesure">确定</div>
          <div class="cancel" @click="cancel">取消</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "selector",
  props: {
    isShow: {
      type: Boolean,
      default: () => {
        return false;
      },
    },
    friendData: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  data() {
    return {
      currentSort: null,
      SelectedFriendList: [],
    };
  },
  created() {},
  mounted() {
  },
  watch: {
    //疑问  为啥深度监听没生效 v-model = item.selected
    friendData: {
      deep: true,
      handler(newObj, oldObj) {
        console.log(newObj)
        console.log(oldObj)
      }
    }
  },
  computed: {   
  },
  methods: {
    // 创建聊天框： 创建群里 或者 创建单聊
    makesure: function() {
      this.$emit("createChat", this.SelectedFriendList);
    },
    cancel: function() {
      this.$emit("close", false);
    },
    unSelected: function(val) {
       for (let i = 0; i < this.friendData.length; i++) {
          if (this.friendData[i].userId == val.userId) {
              this.friendData[i].selected = false;
              this.$set(this.friendData, i, this.friendData[i])
          }
       }
       this.SelectedFriendList.splice(this.SelectedFriendList.findIndex(e => e.userId === val.userId), 1) 
    },
    selectChange: function() {
      this.SelectedFriendList = this.friendData.filter(function(item){
           return item.selected == true;
      })
    },
    close: function () {
      this.$emit("close", false);
    },
    clickUserInfo: function(val, index) {
      let self = this;
      self.currentSort = index; 
    },
  },
};
</script>

<style lang="less" scoped>
.selector-container {
  width: 550px;
  height: 485px;
  background-color: #ffffff;
  position: absolute;
  top: 90px;
  margin: 0 auto;
  left: 700px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.15);
  .left-container {
    width: 50%;
    float: left;
    height: 100%;
    box-sizing: border-box;
    border-right: 1px solid #E7E7E7;
    .searchInput {
      display: inline-block;
      width: 230px;
      height: 20px;
      background-color: #E8E8E8;
      border: none;
      border-radius: 5px;
      margin-top: 20px;
      margin-bottom: 20px;
    }
     .friend-item {
      height: 60px;
      line-height: 60px;
      cursor: pointer;
      display: flex;
      position: relative;
      padding-left: 12px;
      .friend-img {
        width: 40px;
        height: 40px;
        margin: 10px;
        background-size: contain;
      }
      .checkbox-container {
        padding-top: 15px;
        position:absolute;
        right: 30px;
      }
    }
    .selectActive {
      background-color: #DCDAD9;
    }
    .friend-item:hover {
      background-color: #DCDAD9;
    }
  }
  .right-container {
    width: 50%;
    float: left;
    height: 100%;
    .footer-button {
      font-size: 12px;
      position: absolute;
      bottom: 15px;
      right: 15px;
      .makesure {
        width: 70px;
        height: 25px;
        text-align: center;
        line-height: 25px;
        cursor: pointer;
        color: #ffffff;
        background-color: #1AAD19;
        border: 1px solid #E7E7E7;
        float: left;
        margin-right: 10px;
      }
      .makesure:hover {
        background-color: #129611;
      }
      .none-makesure {
        width: 70px;
        height: 25px;
        text-align: center;
        line-height: 25px;
        color: #ffffff;
        background-color: #1AAD19;
        border: 1px solid #E7E7E7;
        float: left;
        margin-right: 10px;
        opacity: 0.5;
      }
      .cancel {
        width: 70px;
        height: 25px;
        text-align: center;
        line-height: 25px;
        cursor: pointer;
        background-color: #ffffff;
        border: 1px solid #E7E7E7;
        float: left;
      }
      .cancel:hover {
        background-color: #EFEFEF;
      }
    }
    .close {
      cursor: pointer;
      text-align: center;
      width: 35px;
      height: 25px;
      float: right;
    }
    .close:hover {
      background: red;
      color: #ffffff;
      border-top-right-radius: 5px;
    }
    .top-information {
      font-size: 12px;
      clear: both;
      text-align: left;
      text-indent: 30px;
    }
      .friend-item {
        height: 60px;
        line-height: 60px;
        cursor: pointer;
        display: flex;
        position: relative;
        padding-left: 12px;
        .friend-img {
          width: 40px;
          height: 40px;
          margin: 10px;
          background-size: contain;
        }
        .checkbox-container {
          padding-top: 15px;
          position:absolute;
          right: 30px;
        }
    }
    .unselect {
      position: absolute;
      right: 20px;
      cursor: pointer;
    }
    .unselect:hover {
      opacity: 0.5;
    }
  }
}
.tui-checkbox:checked {
  background:#1673ff;
}
.tui-checkbox {
	width:25px;
  height:25px;
  outline: none;
	background-color:#ffffff;
	border:solid 1px #dddddd;
	-webkit-border-radius:50%;
	border-radius:50%;
	font-size:0.8rem;
	margin:0;
	padding:0;
	position:relative;
	display:inline-block;
	vertical-align:top;
	cursor:default;
	-webkit-appearance:none;
	-webkit-user-select:none;
	user-select:none;
	-webkit-transition:background-color ease 0.1s;
	transition:background-color ease 0.1s;
}
.tui-checkbox:checked::after {
	content:'';
	top:5px;
	left:5px;
	position:absolute;
	background:transparent;
	border:#fff solid 2px;
	border-top:none;
	border-right:none;
	height:6px;
	width:10px;
	-moz-transform:rotate(-45deg);
	-ms-transform:rotate(-45deg);
	-webkit-transform:rotate(-45deg);
	transform:rotate(-45deg);
}

</style>