<template>
  <view class="container">
    <view class="card">
      <view class="title">健康记录录入</view>

      <picker :range="typeOptions" @change="handleTypeChange">
        <view class="picker-box">
          {{ form.type || '请选择记录类型' }}
        </view>
      </picker>

      <input
        class="input"
        v-model="form.value"
        placeholder="请输入记录值，例如 65.5"
      />

      <input
        class="input"
        v-model="form.unit"
        placeholder="请输入单位，例如 kg"
      />

      <picker mode="date" :value="form.recordDate" :end="today" @change="handleDateChange">
        <view class="picker-box">
          {{ form.recordDate }}
        </view>
      </picker>

      <textarea
        class="textarea"
        v-model="form.remark"
        placeholder="请输入备注"
      />

      <button class="btn" @click="submitRecord">提交记录</button>
    </view>
  </view>
</template>

<script setup>
import { reactive } from 'vue'
import { addHealthRecordApi } from '@/api/health'
import { formatDate } from '@/utils/common'

const typeOptions = ['体重', '血压', '血糖', '睡眠', '心率', '运动']

const today = formatDate()

const form = reactive({
  type: '',
  value: '',
  unit: '',
  recordDate: formatDate(),
  remark: ''
})

const handleTypeChange = (e) => {
  form.type = typeOptions[e.detail.value]
}

const handleDateChange = (e) => {
  form.recordDate = e.detail.value
}

const resetForm = () => {
  form.type = ''
  form.value = ''
  form.unit = ''
  form.recordDate = formatDate()
  form.remark = ''
}

const submitRecord = async () => {
  if (!form.type || !form.value || !form.unit) {
    uni.showToast({
      title: '请填写完整信息',
      icon: 'none'
    })
    return
  }

  try {
    const res = await addHealthRecordApi({
      userId: 1,
      type: form.type,
      value: form.value,
      unit: form.unit,
      recordDate: form.recordDate,
      remark: form.remark
    })

    if (res.code === 200) {
      uni.showToast({
        title: '提交成功',
        icon: 'success'
      })
      resetForm()
      uni.navigateTo({
        url: '/pages/health/list'
      })
    } else {
      uni.showToast({
        title: res.message || '提交失败',
        icon: 'none'
      })
    }
  } catch (e) {
    uni.showToast({
      title: '提交失败',
      icon: 'none'
    })
    console.log(e)
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 24rpx;
  box-sizing: border-box;
}

.card {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}

.input,
.picker-box {
  height: 88rpx;
  border: 1rpx solid #e5e6eb;
  border-radius: 12rpx;
  padding: 0 24rpx;
  margin-bottom: 24rpx;
  display: flex;
  align-items: center;
  color: #333;
  background: #fafafa;
}

.textarea {
  width: 100%;
  min-height: 180rpx;
  border: 1rpx solid #e5e6eb;
  border-radius: 12rpx;
  padding: 20rpx 24rpx;
  box-sizing: border-box;
  margin-bottom: 24rpx;
  background: #fafafa;
}

.btn {
  background: #3c9cff;
  color: #fff;
  border-radius: 12rpx;
}
</style>