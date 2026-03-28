<template>
  <view class="container">
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
import { ref, onMounted } from 'vue'
import { getTrendApi } from '@/api/health'

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
  try {
    const res = await getTrendApi(1, '体重')

    console.log("趋势数据:", res)

    chartData.value = {
      categories: res.map(i => i.record_date),
      series: [
        {
          name: "体重",
          data: res.map(i => Number(i.value))
        }
      ]
    }

  } catch (err) {
    console.log(err)
  }
}

import { onShow } from '@dcloudio/uni-app'

onShow(() => {
  loadTrend()
})
</script>

<style>
.container {
  padding: 20rpx;
}
</style>