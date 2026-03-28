<template>
  <view class="container">
    <view class="card">
      <view class="title">你好，{{ userInfo.nickname || userInfo.username || '用户' }}</view>
      <view class="desc">欢迎使用个人健康管理 App</view>
    </view>

    <view class="card">
      <view class="section-title">快捷功能</view>
      <view class="grid">
        <view class="grid-item" @click="goRecord">健康录入</view>
        <view class="grid-item" @click="goTrend">趋势分析</view>
        <view class="grid-item" @click="showDev">风险评估</view>
        <view class="grid-item" @click="showDev">健康计划</view>
		<view class="grid-item" @click="goRisk">风险评估</view>
		<view class="grid-item" @click="goList">健康记录</view>
      </view>
    </view>

    <view class="card">
      <view class="section-title">用户信息</view>
      <view class="row"><text>用户ID：</text><text>{{ userInfo.id || '-' }}</text></view>
      <view class="row"><text>用户名：</text><text>{{ userInfo.username || '-' }}</text></view>
      <view class="row"><text>昵称：</text><text>{{ userInfo.nickname || '-' }}</text></view>
    </view>
  </view>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { getProfileApi } from '@/api/user'
import { getUserInfo, setUserInfo } from '@/utils/auth'

const userInfo = reactive({
  id: '',
  username: '',
  nickname: ''
})

const loadUserInfo = async () => {
  const cacheUser = getUserInfo()
  if (cacheUser && cacheUser.id) {
    Object.assign(userInfo, cacheUser)
  }

  try {
    const res = await getProfileApi()
    if (res.code === 200) {
      Object.assign(userInfo, res.data)
      setUserInfo(res.data)
    }
  } catch (e) {}
}

const goRecord = () => {
  uni.switchTab({
    url: '/pages/health/record'
  })
}

const goTrend = ()=>{
  uni.navigateTo({
    url:'/pages/trend/trend'
  })
}

const goRisk = ()=>{
  uni.navigateTo({
    url:'/pages/risk/risk'
  })
}

const goList = ()=>{
  uni.navigateTo({
    url:'/pages/health/list'
  })
}

const showDev = () => {
  uni.showToast({
    title: '功能开发中',
    icon: 'none'
  })
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 24rpx;
  box-sizing: border-box;
}

.card {
  background: #fff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 24rpx;
}

.title {
  font-size: 38rpx;
  font-weight: bold;
  color: #333;
}

.desc {
  font-size: 26rpx;
  color: #888;
  margin-top: 12rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  margin-bottom: 24rpx;
}

.grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.grid-item {
  width: calc(50% - 10rpx);
  background: #f0f7ff;
  color: #3c9cff;
  text-align: center;
  border-radius: 16rpx;
  padding: 30rpx 0;
  font-size: 28rpx;
  font-weight: 600;
}

.row {
  display: flex;
  justify-content: space-between;
  padding: 18rpx 0;
  border-bottom: 1rpx solid #f2f2f2;
  font-size: 28rpx;
  color: #555;
}
</style>