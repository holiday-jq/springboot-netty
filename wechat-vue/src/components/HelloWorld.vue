<template>
  <div class="wechat-container">
       <div>聊天室</div>
  </div>
</template>

<script>
import * as request from "@/utils/request"
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data() {
    return {
      "hehe": "111"
    }
  },
  created() {
     console.log(request)
     console.log(this.hehe)
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">

</style>
