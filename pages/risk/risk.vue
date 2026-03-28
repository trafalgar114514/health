<template>
  <view class="container">
    <view class="card">
      <view class="title">健康风险评估</view>

      <input v-model="form.age" placeholder="年龄" class="input" />
      <input v-model="form.height" placeholder="身高 cm" class="input" />
      <input v-model="form.weight" placeholder="体重 kg" class="input" />
      <input v-model="form.sbp" placeholder="收缩压" class="input" />
      <input v-model="form.dbp" placeholder="舒张压" class="input" />

      <button @tap="evaluate" class="btn">开始评估</button>
      <button @tap="goBack" class="btn gray">返回</button>
    </view>

    <view v-if="result" class="result card">
      <view>风险等级：{{ result.level }}</view>
      <view>建议：{{ result.advice }}</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { riskApi } from '@/api/risk'

const form = ref({
  age: '',
  height: '',
  weight: '',
  sbp: '',
  dbp: ''
})

const result = ref(null)

const evaluate = async () => {
  if (!form.value.age || !form.value.height || !form.value.weight || !form.value.sbp || !form.value.dbp) {
    uni.showToast({
      title: '请填写完整信息',
      icon: 'none'
    })
    return
  }

  try {
    const res = await riskApi(form.value)
    result.value = res.data
  } catch (e) {
    console.log('风险评估失败', e)
    uni.showToast({
      title: '评估失败',
      icon: 'none'
    })
  }
}

const goBack = () => {
  uni.navigateBack()
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 30rpx;
  box-sizing: border-box;
}

.card {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.title {
  font-size: 38rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}

.input {
  height: 80rpx;
  border: 1px solid #ddd;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
  padding: 0 20rpx;
  box-sizing: border-box;
  background: #fff;
}

.btn {
  margin-top: 20rpx;
  background: #007aff;
  color: #fff;
}

.gray {
  background: #999;
}

.result {
  font-size: 30rpx;
  line-height: 1.8;
}
</style>