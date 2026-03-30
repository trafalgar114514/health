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
        :opts="bpOpts"
        :chartData="bpChartData"
        style="width: 100%; height: 300px;"
      />
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getHealthListApi, getTrendApi } from '@/api/health'

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

const bpOpts = {
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
  if (Array.isArray(res?.data?.data)) return res.data.data
  if (Array.isArray(res?.data?.list)) return res.data.list
  return []
}

const parseBpValue = (item) => {
  if (
    item.systolic !== null &&
    item.systolic !== undefined &&
    item.diastolic !== null &&
    item.diastolic !== undefined
  ) {
    return {
      high: Number(item.systolic),
      low: Number(item.diastolic)
    }
  }

  const str = String(item.value || '').trim()
  const match = str.match(/(\d+)\s*\/\s*(\d+)/)

  if (match) {
    return {
      high: Number(match[1]),
      low: Number(match[2])
    }
  }

  const num = Number(str)
  if (!Number.isNaN(num)) {
    return {
      high: num,
      low: null
    }
  }

  return {
    high: null,
    low: null
  }
}

const loadSingleTrend = async (userId, type, name) => {
  const res = await getTrendApi(userId, type)
  const arr = normalizeTrendList(res)
  const getDate = (item) => item.record_date || item.recordDate || ''

  return {
    categories: arr.map(item => String(getDate(item)).slice(0, 10)),
    series: [
      {
        name,
        data: arr.map(item => Number(item.value))
      }
    ]
  }
}

const loadBpTrend = async (userId) => {
  const [systolicRes, diastolicRes] = await Promise.all([
    getTrendApi(userId, '收缩压'),
    getTrendApi(userId, '舒张压')
  ])

  const systolicList = normalizeTrendList(systolicRes)
  const diastolicList = normalizeTrendList(diastolicRes)

  const getDate = (item) => item.record_date || item.recordDate || ''

  const dateMap = {}

  systolicList.forEach(item => {
    const date = String(getDate(item)).slice(0, 10)
    const value = Number(item.value)
    if (!date || Number.isNaN(value)) return

    if (!dateMap[date]) {
      dateMap[date] = {
        systolic: null,
        diastolic: null
      }
    }
    dateMap[date].systolic = value
  })

  diastolicList.forEach(item => {
    const date = String(getDate(item)).slice(0, 10)
    const value = Number(item.value)
    if (!date || Number.isNaN(value)) return

    if (!dateMap[date]) {
      dateMap[date] = {
        systolic: null,
        diastolic: null
      }
    }
    dateMap[date].diastolic = value
  })

  const categories = Object.keys(dateMap).sort(
    (a, b) => new Date(a).getTime() - new Date(b).getTime()
  )

  return {
    categories,
    series: [
      {
        name: '收缩压',
        data: categories.map(date => dateMap[date].systolic)
      },
      {
        name: '舒张压',
        data: categories.map(date => dateMap[date].diastolic)
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
      console.log('体重结果=', results[0])
      console.log('身高结果=', results[1])
      console.log('血压结果=', results[2])
      console.log('血压失败原因=', results[2]?.reason)
    
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
  uni.switchTab({
    url: '/pages/index/index'
  })
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
