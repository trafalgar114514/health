<template>
  <view class="container">
    <view class="title">健康录入</view>

    <input v-model="form.type" class="input" placeholder="请输入类型，如体重/血压" />
    <input v-model="form.value" class="input" placeholder="请输入数值" type="digit" />
    <input v-model="form.unit" class="input" placeholder="请输入单位，如kg/mmHg" />
    <input v-model="form.recordDate" class="input" placeholder="请输入日期，如2026-03-28" />
    <input v-model="form.remark" class="input" placeholder="备注" />

    <button class="btn" @click="handleSubmit">提交</button>
  </view>
</template>

<script setup>
import { reactive } from 'vue'
import request from '@/utils/request'

const userInfo = uni.getStorageSync('userInfo') || {}

const form = reactive({
  userId: userInfo.id || '',
  type: '',
  value: '',
  unit: '',
  recordDate: '',
  remark: ''
})

const handleSubmit = async () => {
  if (!form.userId) {
    uni.showToast({
      title: '用户未登录',
      icon: 'none'
    })
    return
  }

  if (!form.type || !form.value || !form.recordDate) {
    uni.showToast({
      title: '请填写完整信息',
      icon: 'none'
    })
    return
  }

  try {
    const res = await request({
      url: '/health/add',
      method: 'POST',
      data: {
        userId: form.userId,
        type: form.type,
        value: Number(form.value),
        unit: form.unit,
        recordDate: form.recordDate,
        remark: form.remark
      }
    })

    if (res.code === 200) {
      uni.showToast({
        title: '提交成功',
        icon: 'success'
      })

      form.type = ''
      form.value = ''
      form.unit = ''
      form.recordDate = ''
      form.remark = ''
    }
  } catch (e) {
    console.log('提交失败', e)
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 40rpx;
  box-sizing: border-box;
}

.title {
  font-size: 40rpx;
  font-weight: bold;
  text-align: center;
  margin-bottom: 40rpx;
}

.input {
  height: 80rpx;
  border: 1px solid #ddd;
  border-radius: 12rpx;
  padding: 0 20rpx;
  margin-bottom: 20rpx;
  background: #fff;
  box-sizing: border-box;
}

.btn {
  margin-top: 20rpx;
  background: #007aff;
  color: #fff;
}
</style>