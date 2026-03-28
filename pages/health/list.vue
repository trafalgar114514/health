<template>
  <view class="container">
    <view
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
import { ref, onMounted } from 'vue'
import { getHealthListApi } from '@/api/health'

const list = ref([])

const loadList = async () => {
  try {
    const res = await getHealthListApi(1)
    list.value = res
  } catch (e) {
    console.log('加载健康记录失败', e)
  }
}

onMounted(() => {
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
</style>