<template>
  <view class="container">
    <view class="card user-card">
      <view class="avatar">{{ firstWord }}</view>
      <view>
        <view class="name">{{ userInfo.nickname || userInfo.username || '用户' }}</view>
        <view class="tip">管理你的健康档案与设置</view>
      </view>
    </view>

    <view class="card">
      <view class="item"><text>用户ID</text><text>{{ userInfo.id || '-' }}</text></view>
      <view class="item"><text>用户名</text><text>{{ userInfo.username || '-' }}</text></view>
      <view class="item"><text>昵称</text><text>{{ userInfo.nickname || '-' }}</text></view>
    </view>

    <view class="card">
      <view class="menu" @click="showDev">健康档案</view>
      <view class="menu" @click="showDev">家庭成员管理</view>
      <view class="menu" @click="showDev">消息中心</view>
      <view class="menu logout" @click="logout">退出登录</view>
    </view>
  </view>
</template>

<script setup>
import { computed, reactive, onMounted } from 'vue'
import { getProfileApi } from '@/api/user'
import { getUserInfo, setUserInfo, clearToken } from '@/utils/auth'

const userInfo = reactive({
  id: '',
  username: '',
  nickname: ''
})

const firstWord = computed(() => {
  const text = userInfo.nickname || userInfo.username || 'U'
  return text.slice(0, 1)
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

const showDev = () => {
  uni.showToast({
    title: '功能开发中',
    icon: 'none'
  })
}

const logout = () => {
  clearToken()
  uni.reLaunch({
    url: '/pages/login/login'
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

.user-card {
  display: flex;
  align-items: center;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: #3c9cff;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-right: 24rpx;
}

.name {
  font-size: 34rpx;
  font-weight: bold;
  color: #333;
}

.tip {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}

.item,
.menu {
  display: flex;
  justify-content: space-between;
  padding: 22rpx 0;
  border-bottom: 1rpx solid #f2f2f2;
  font-size: 28rpx;
  color: #555;
}

.logout {
  color: #f56c6c;
}
</style>