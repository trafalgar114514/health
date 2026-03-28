<template>
  <view class="container">
    <button class="back-btn" @click="goBack">返回</button>

    <view class="card" v-for="item in list" :key="item.id">
      <view class="row">
        <text class="type">{{ item.type }}</text>
        <text class="value">{{ item.value }}{{ item.unit || '' }}</text>
      </view>

      <view class="date">
        {{ item.recordDate }}
      </view>

      <view class="remark" v-if="item.remark">
        {{ item.remark }}
      </view>
    </view>

    <view v-if="list.length === 0" class="empty">
      暂无健康记录
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getHealthListApi } from '@/api/health'

const list = ref([])
const userInfo = uni.getStorageSync('userInfo') || {}

const loadList = async () => {
  if (!userInfo.id) {
    uni.showToast({
      title: '请先登录',
      icon: 'none'
    })
    return
  }

  try {
    const res = await getHealthListApi(userInfo.id)
    list.value = res.data || []
  } catch (e) {
    console.log('加载健康记录失败', e)
    uni.showToast({
      title: '加载失败',
      icon: 'none'
    })
  }
}

const goBack = () => {
  uni.navigateBack()
}

onMounted(() => {
  loadList()
})
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  box-sizing: border-box;
}

.back-btn {
  margin-bottom: 20rpx;
  background: #007aff;
  color: #fff;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.row {
  display: flex;
  justify-content: space-between;
  font-size: 32rpx;
}

.type {
  font-weight: bold;
}

.value {
  color: #3c9cff;
}

.date {
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}

.remark {
  margin-top: 10rpx;
  font-size: 26rpx;
}

.empty {
  text-align: center;
  color: #999;
  margin-top: 60rpx;
}
</style>