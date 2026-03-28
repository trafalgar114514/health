<template>
  <view class="page">
    <view class="card">
      <view class="title">新增健康记录</view>

      <view class="form-item">
        <text class="label">类型</text>
        <picker :range="recordTypeNames" @change="onTypeChange">
          <view class="picker">{{ currentType.label }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">数值</text>
        <input
          v-model="form.value"
          type="digit"
          class="input"
          :placeholder="`请输入${currentType.label}`"
        />
      </view>

      <view class="form-item">
        <text class="label">单位</text>
        <input class="input disabled" :value="currentType.unit" disabled />
      </view>

      <view class="form-item">
        <text class="label">日期</text>
        <picker mode="date" :value="form.date" :end="today" @change="onDateChange">
          <view class="picker">{{ form.date }}</view>
        </picker>
      </view>

      <button class="submit" type="primary" @click="saveRecord">保存记录</button>
    </view>
  </view>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { addHealthRecord } from '@/utils/healthRecordDb'

const recordTypes = [
  { value: 'height', label: '身高', unit: 'cm' },
  { value: 'weight', label: '体重', unit: 'kg' },
  { value: 'blood_pressure', label: '血压', unit: 'mmHg' }
]

const typeIndex = ref(0)
const today = new Date().toISOString().slice(0, 10)
const form = reactive({
  type: recordTypes[0].value,
  value: '',
  unit: recordTypes[0].unit,
  date: today
})

const recordTypeNames = computed(() => recordTypes.map((item) => item.label))
const currentType = computed(() => recordTypes[typeIndex.value])

const onTypeChange = (event) => {
  const index = Number(event.detail.value)
  typeIndex.value = index
  form.type = recordTypes[index].value
  form.unit = recordTypes[index].unit
}

const onDateChange = (event) => {
  form.date = event.detail.value
}

const saveRecord = () => {
  if (!form.value) {
    uni.showToast({ title: '请输入数值', icon: 'none' })
    return
  }

  addHealthRecord({
    type: form.type,
    typeLabel: currentType.value.label,
    value: form.value,
    unit: form.unit,
    date: form.date,
    createdAt: new Date().toISOString()
  })

  uni.showToast({ title: '保存成功', icon: 'success' })
  form.value = ''
}
</script>

<style scoped>
.page {
  padding: 24rpx;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.05);
}

.title {
  font-size: 36rpx;
  font-weight: 700;
  margin-bottom: 24rpx;
}

.form-item {
  margin-bottom: 24rpx;
}

.label {
  display: block;
  color: #555;
  margin-bottom: 12rpx;
}

.input,
.picker {
  min-height: 80rpx;
  padding: 0 20rpx;
  border: 1px solid #dcdfe6;
  border-radius: 10rpx;
  display: flex;
  align-items: center;
  background: #fff;
}

.disabled {
  color: #909399;
  background: #f5f7fa;
}

.submit {
  margin-top: 10rpx;
}
</style>
