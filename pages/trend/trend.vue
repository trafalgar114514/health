<template>
  <view class="container">
    <button class="back-btn" @click="goBack">返回</button>

    <view class="chart-card">
      <view class="chart-title">体重趋势</view>
      <qiun-data-charts
        type="line"
        canvas2d
        canvas-id="weightTrendChart"
        :opts="opts"
        :chartData="weightChartData"
        style="width: 100%; height: 300px;"
      />
    </view>

    <view class="chart-card">
      <view class="chart-title">身高趋势</view>
      <qiun-data-charts
        type="line"
        canvas2d
        canvas-id="heightTrendChart"
        :opts="opts"
        :chartData="heightChartData"
        style="width: 100%; height: 300px;"
      />
    </view>

    <view class="chart-card">
      <view class="chart-title">血压趋势</view>
      <qiun-data-charts
        type="line"
        canvas2d
        canvas-id="bpTrendChart"
        :opts="opts"
        :chartData="bpChartData"
        style="width: 100%; height: 300px;"
      />
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getTrendApi } from '@/api/health'

const createEmptyChartData = () => ({
  categories: [],
  series: []
})

const weightChartData = ref(createEmptyChartData())
const heightChartData = ref(createEmptyChartData())
const bpChartData = ref(createEmptyChartData())

const opts = {
  color: ['#1890FF', '#91CB74', '#FAC858'],
  padding: [15, 15, 0, 5],
  enableScroll: false,
  legend: {
    show: true
  },
  xAxis: {
    disableGrid: true,
    rotateLabel: true
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

const normalizeTrendList = (res) => {
  if (Array.isArray(res)) return res
  if (Array.isArray(res?.data)) return res.data
  return []
}

const loadSingleTrend = async (userId, type, name) => {
  const res = await getTrendApi(userId, type)
  const arr = normalizeTrendList(res)

  return {
    categories: arr.map(item => item.record_date),
    series: [
      {
        name,
        data: arr.map(item => Number(item.value))
      }
    ]
  }
}

const loadBpTrend = async (userId) => {
  const res = await getTrendApi(userId, '血压')
  const arr = normalizeTrendList(res)

  const categories = arr.map(item => item.record_date)

  const highData = arr.map(item => {
    if (typeof item.value === 'string' && item.value.includes('/')) {
      return Number(item.value.split('/')[0])
    }
    return Number(item.value)
  })

  const lowData = arr.map(item => {
    if (typeof item.value === 'string' && item.value.includes('/')) {
      return Number(item.value.split('/')[1])
    }
    return null
  })

  const hasLow = lowData.some(v => v !== null && !Number.isNaN(v))

  return {
    categories,
    series: hasLow
      ? [
          {
            name: '收缩压',
            data: highData
          },
          {
            name: '舒张压',
            data: lowData
          }
        ]
      : [
          {
            name: '血压',
            data: highData
          }
        ]
  }
}

const loadTrend = async () => {
  const userId = 1

  try {
    const results = await Promise.allSettled([
      loadSingleTrend(userId, '体重', '体重'),
      loadSingleTrend(userId, '身高', '身高'),
      loadBpTrend(userId)
    ])

    console.log('results=', results)

    weightChartData.value =
      results[0].status === 'fulfilled'
        ? results[0].value
        : createEmptyChartData()

    heightChartData.value =
      results[1].status === 'fulfilled'
        ? results[1].value
        : createEmptyChartData()

    bpChartData.value =
      results[2].status === 'fulfilled'
        ? results[2].value
        : createEmptyChartData()

    if (results.some(item => item.status === 'rejected')) {
      console.log('失败详情=', results)
      uni.showToast({
        title: '部分趋势加载失败',
        icon: 'none'
      })
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

.chart-card {
  background: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.chart-title {
  font-size: 30rpx;
  font-weight: 600;
  margin-bottom: 12rpx;
  color: #333;
}
</style>
