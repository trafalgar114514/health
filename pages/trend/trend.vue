<template>
  <view class="container">
    <button class="back-btn" @click="goBack">返回</button>

    <qiun-data-charts
      type="line"
      canvas2d
      canvas-id="trendChart"
      :opts="opts"
      :chartData="chartData"
    />
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getTrendApi } from '@/api/health'

const userInfo = uni.getStorageSync('userInfo') || {}

const chartData = ref({
  categories: [],
  series: []
})

const opts = {
  legend: {},
  xAxis: {
    disableGrid: true
  },
  yAxis: {
    gridType: 'dash',
    dashLength: 2
  },
  extra: {
    line: {
      type: 'straight',
      width: 2
    }
  }
}

const loadTrend = async () => {
  if (!userInfo.id) {
    uni.showToast({
      title: '请先登录',
      icon: 'none'
    })
    return
  }

  try {
    const res = await getTrendApi(userInfo.id, '体重')
    const arr = res.data || []

    chartData.value = {
      categories: arr.map(i => i.recordDate),
      series: [
        {
          name: '体重',
          data: arr.map(i => Number(i.value))
        }
      ]
    }
  } catch (err) {
    console.log('趋势加载失败', err)
    uni.showToast({
      title: '趋势加载失败',
      icon: 'none'
    })
  }
}

const goBack = () => {
  uni.navigateBack()
}

onShow(() => {
  loadTrend()
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
</style>