<template>
     <div class="expression-container" v-show="isShowExpression">
         <div class="expressionItem" v-for="(item, index) in expresionData" :key="index" 
              :title="item.title" @click="selectExpression(item.title, item.name)">
              <img class="imgClass" :src="require('../assets/expression/'+item.name)">
         </div>
     </div>
</template>

<script>
import * as constant from '@/constant/expressionData';
export default {
    name: "expression",
    props: {
        isShowExpression: {
            type: Boolean,
            default: () => {
                return false;
            },
        },       
    },
    data() {
        return {
            expresionData: constant.expresionData,
        }
    },
    created() {    
    },
    mounted() {
        console.log("expression")
    },
    methods: {
        selectExpression: function(title, name) {
            let params = {
                title: title,
                name: name
            }
            this.$emit("seleceExpress", params);
        }
    },
}
</script>

<style lang="less" scoped>
   .expression-container {
       width: 480px;
       height: 288px;
       background-color: #fff;
       position: absolute;
       top: -289px;
       left: -95px;
       padding: 20px;
       box-shadow: 0 0 8px rgba(0, 0, 0, 0.15);
       box-sizing: border-box;
       .expressionItem {
           width: 36px;
           height: 36px;
           text-align: center;
           line-height: 36px;
           float: left;
           .imgClass {
                width: 25px;
                height: 25px;
                padding-top: 5px;
                cursor: pointer;
           }
       }
       .expressionItem:hover {
           background-color: #E7E7E7;
       }
   }
</style>