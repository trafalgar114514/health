<template>
  <view class="page">
    <view class="card" v-for="item in records" :key="item.id">
      <view class="row">
        <text class="type">{{ item.typeLabel }}</text>
        <text class="date">{{ item.date }}</text>
      </view>
      <view class="value">{{ item.value }} {{ item.unit }}</view>
    </view>
    <view v-if="!records.length" class="empty">暂无记录</view>
  </view>
</template>

<script setup>
import { onShow } from '@dcloudio/uni-app'
import { ref } from 'vue'
import { getHealthRecords } from '@/utils/healthRecordDb'

const records = ref([])

onShow(() => {
  records.value = getHealthRecords()
})
</script>

<style scoped>
.page { padding: 24rpx; }
.card { background: #fff; border-radius: 12rpx; padding: 20rpx; margin-bottom: 16rpx; }
.row { display: flex; justify-content: space-between; margin-bottom: 8rpx; }
.type { font-weight: 600; }
.date { color: #888; font-size: 24rpx; }
.value { color: #333; font-size: 32rpx; }
.empty { text-align: center; color: #999; margin-top: 80rpx; }
</style>
