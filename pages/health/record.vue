<template>
  <view class="container">
    <view class="title">健康录入</view>

    <picker class="picker" :range="typeOptions" :value="typeIndex" @change="handleTypeChange">
      <view class="picker-value" :class="{ placeholder: !form.type }">
        {{ form.type || '请选择类型' }}
      </view>
    </picker>
    <template v-if="form.type === '血压'">
      <view class="bp-row">
        <input v-model="form.systolic" class="input bp-input" placeholder="收缩压" type="digit" />
        <text class="slash">/</text>
        <input v-model="form.diastolic" class="input bp-input" placeholder="舒张压" type="digit" />
      </view>
    </template>
    <input v-else v-model="form.value" class="input" placeholder="请输入数值" type="digit" />
    <input v-model="form.unit" class="input" placeholder="单位将自动选择" disabled />
    <picker mode="date" :value="form.recordDate" :end="today" @change="handleDateChange">
      <view class="picker-value" :class="{ placeholder: !form.recordDate }">
        {{ form.recordDate || '请选择日期' }}
      </view>
    </picker>
    <input v-model="form.remark" class="input" placeholder="备注" />

    <button class="btn" @click="handleSubmit">提交</button>
  </view>
</template>

<script setup>
import { computed, reactive } from 'vue'
import request from '@/utils/request'

const userInfo = uni.getStorageSync('userInfo') || {}
const typeOptions = ['身高', '体重', '血压']
const unitMap = {
  身高: 'cm',
  体重: 'kg',
  血压: 'mmHg'
}

const getToday = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = `${now.getMonth() + 1}`.padStart(2, '0')
  const day = `${now.getDate()}`.padStart(2, '0')
  return `${year}-${month}-${day}`
}

const today = getToday()

const form = reactive({
  userId: userInfo.id || '',
  type: '',
  value: '',
  systolic: '',
  diastolic: '',
  unit: '',
  recordDate: '',
  remark: ''
})

const typeIndex = computed(() => typeOptions.findIndex((item) => item === form.type))

const resetForm = () => {
  form.type = ''
  form.value = ''
  form.systolic = ''
  form.diastolic = ''
  form.unit = ''
  form.recordDate = ''
  form.remark = ''
}

const handleTypeChange = (e) => {
  const index = Number(e.detail.value)
  form.type = typeOptions[index]
  form.unit = unitMap[form.type] || ''
  form.value = ''
  form.systolic = ''
  form.diastolic = ''
}

const handleDateChange = (e) => {
  form.recordDate = e.detail.value
}

const addHealthRecord = (data) => {
  return request({
    url: '/health/add',
    method: 'POST',
    data
  })
}

const handleSubmit = async () => {
  if (!form.userId) {
    uni.showToast({
      title: '用户未登录',
      icon: 'none'
    })
    return
  }

  if (!form.type) {
    uni.showToast({
      title: '请选择类型',
      icon: 'none'
    })
    return
  }

  if (!form.recordDate) {
    uni.showToast({
      title: '请选择日期',
      icon: 'none'
    })
    return
  }

  try {
    if (form.type === '血压') {
      if (!form.systolic || !form.diastolic) {
        uni.showToast({
          title: '请输入收缩压和舒张压',
          icon: 'none'
        })
        return
      }

      await addHealthRecord({
        userId: form.userId,
        type: '收缩压',
        value: Number(form.systolic),
        unit: 'mmHg',
        recordDate: form.recordDate,
        remark: form.remark
      })

      await addHealthRecord({
        userId: form.userId,
        type: '舒张压',
        value: Number(form.diastolic),
        unit: 'mmHg',
        recordDate: form.recordDate,
        remark: form.remark
      })
    } else {
      if (!form.value) {
        uni.showToast({
          title: '请输入数值',
          icon: 'none'
        })
        return
      }

      await addHealthRecord({
        userId: form.userId,
        type: form.type,
        value: Number(form.value),
        unit: unitMap[form.type] || form.unit,
        recordDate: form.recordDate,
        remark: form.remark
      })
    }

    uni.showToast({
      title: '提交成功',
      icon: 'success'
    })

    resetForm()
  } catch (e) {
    console.log('提交失败', e)
    uni.showToast({
      title: '提交失败',
      icon: 'none'
    })
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

.bp-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.bp-input {
  flex: 1;
}

.slash {
  font-size: 34rpx;
  color: #666;
  margin-bottom: 20rpx;
}

.picker {
  margin-bottom: 20rpx;
}

.picker-value {
  height: 80rpx;
  border: 1px solid #ddd;
  border-radius: 12rpx;
  padding: 0 20rpx;
  background: #fff;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  color: #333;
}

.placeholder {
  color: #999;
}

.btn {
  margin-top: 20rpx;
  background: #007aff;
  color: #fff;
}
</style>
