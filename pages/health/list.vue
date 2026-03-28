<template>
  <view class="container">
    <view v-if="loading" class="state">加载中...</view>

    <view v-else-if="list.length === 0" class="state empty">
      暂无健康记录，先去录入一条吧
    </view>

    <view
      v-else
      class="card"
      v-for="item in list"
      :key="item.id"
    >
      <view class="row">
        <text class="type">{{ item.type }}</text>
        <text class="value">{{ item.value }}{{ item.unit }}</text>
      </view>

      <view class="date">
        {{ item.recordDate }}
      </view>

      <view class="remark" v-if="item.remark">
        {{ item.remark }}
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getHealthListApi } from '@/api/health'
import { getUserInfo } from '@/utils/auth'

const list = ref([])
const loading = ref(false)

const loadList = async () => {
  const userInfo = getUserInfo() || {}
  const userId = userInfo.id || 1

  loading.value = true
  try {
    const res = await getHealthListApi(userId)
    list.value = Array.isArray(res) ? res : []
  } catch (e) {
    list.value = []
    uni.showToast({
      title: '加载健康记录失败',
      icon: 'none'
    })
    console.log('加载健康记录失败', e)
  } finally {
    loading.value = false
  }
}

onShow(() => {
  loadList()
})
</script>

<style scoped>
.container {
  padding: 20rpx;
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

.state {
  margin-top: 120rpx;
  text-align: center;
  color: #666;
  font-size: 28rpx;
}

.empty {
  color: #999;
}
</style>
