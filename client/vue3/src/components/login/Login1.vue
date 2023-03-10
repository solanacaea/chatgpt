<script setup lang="ts">
import { useRoute } from 'vue-router';
const route = useRoute();
const conversation = route.params.conversation;

export default {
  name: "login", 
  data() {
    return {
      form: {
        username: "admin",
        password: "admin",
      },
    };
  },
  methods: {
    onSubmit() {
      axios.request({
        url: '/api/chat',
        method: 'POST',
        data: { 
          "username": form.username,
          "password": form.password
        },
      }).then((res) => {
        if (res.code == 200) {
          res.accessToken
          res.tokenType
          router.push("/" + conversation)
        } else {
          console.log(res.msg)
        }
      });
    },
  },
};
</script>

<template>
  <div class="login-container">
    <h2 class="login-title">我的登陆页面</h2>
    <a-form ref="form" :model="form" class="login-form">
      <h2 class="title">用户登录 LOGIN</h2>
      <a-form-item>
        <a-input v-model="form.username">
          <a-icon slot="prefix" type="user" />
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-input-password v-model="form.password">
          <a-icon slot="prefix" type="unlock" />
        </a-input-password>
      </a-form-item>
      <a-form-item>
        <a-button class="submit" type="primary" @click="onSubmit">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
  <div class="loginFooter">
      © 2018-2021 CoreCmd 版权所有. 沪ICP备号-1 沪公网安备 52038202001416号
  </div>
</template>

<style scoped>
.login-form {
  width: 565px;
  height: 372px;
  margin: 0 auto;
  background: url("../../assets/houTaiKuang.png");
  padding: 40px 110px;
}

/* 背景 */
.login-container {
  position: absolute;
  width: 100%;
  height: 100%;
  background: url("../../assets/mcdku.jpg") no-repeat;
  background-size: 100% 100%;
}

/* Log */
.login-title {
  color: #fff;
  text-align: center;
  margin: 100px 0 60px 0;
  font-size: 36px;
  font-family: Microsoft Yahei;
}
/* 登陆按钮 */
.submit {
  width: 100%;
  height: 45px;
  font-size: 16px;
}
/* 用户登陆标题 */
.title {
  margin-bottom: 50px;
  color: #fff;
  font-weight: 700;
  font-size: 24px;
  font-family: Microsoft Yahei;
}
/* 输入框 */
.inputBox {
  height: 45px;
}
/* 输入框内左边距50px */
.ant-input-affix-wrapper .ant-input:not(:first-child) {
  padding-left: 50px;
}
.loginFooter{
      width: 100%;
      min-height: 64px;
      display: flex;
      justify-content: center;
      align-items: flex-start;
      flex-direction: row;
      text-align: center;
      color: #ffffff;
      margin-top: 40px;
  }
</style>
  