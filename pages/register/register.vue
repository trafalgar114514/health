<template>
  <view class="container">
    <view class="box">
      <view class="title">用户注册</view>

      <input v-model="form.username" class="input" placeholder="请输入用户名" />
      <input v-model="form.password" class="input" password placeholder="请输入密码" />
      <input v-model="form.confirmPassword" class="input" password placeholder="请确认密码" />

      <button class="btn" @click="handleRegister">注册</button>
      <button class="btn gray" @click="goLogin">去登录</button>
    </view>
  </view>
</template>

<script setup>
import { reactive } from 'vue'
import { registerApi } from '@/api/user'

const form = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

const handleRegister = async () => {
  if (!form.username || !form.password) {
    uni.showToast({
      title: '用户名和密码不能为空',
      icon: 'none'
    })
    return
  }

  if (form.password !== form.confirmPassword) {
    uni.showToast({
      title: '两次密码不一致',
      icon: 'none'
    })
    return
  }

  try {
    const res = await registerApi({
      username: form.username,
      password: form.password
    })

    if (res.code === 200) {
      uni.showToast({
        title: '注册成功',
        icon: 'success'
      })

      setTimeout(() => {
        uni.navigateTo({
          url: '/pages/login/login'
        })
      }, 800)
    }
  } catch (e) {
    console.log('注册失败', e)
  }
}

const goLogin = () => {
  uni.navigateTo({
    url: '/pages/login/login'
  })
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 40rpx;
  box-sizing: border-box;
}
.box {
  background: #fff;
  padding: 40rpx;
  border-radius: 20rpx;
  margin-top: 80rpx;
}
.title {
  font-size: 40rpx;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30rpx;
}
.input {
  height: 80rpx;
  border: 1px solid #ddd;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
  padding: 0 20rpx;
  box-sizing: border-box;
  background: #fff;
}
.btn {
  margin-top: 20rpx;
  background: #007aff;
  color: #fff;
}
.gray {
  background: #007aff;
}
</style>