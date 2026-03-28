<template>
  <view class="container">

    <view class="card">
      <view class="title">健康风险评估</view>

      <input v-model="form.age" placeholder="年龄"/>
      <input v-model="form.height" placeholder="身高 cm"/>
      <input v-model="form.weight" placeholder="体重 kg"/>
      <input v-model="form.sbp" placeholder="收缩压"/>
      <input v-model="form.dbp" placeholder="舒张压"/>

      <button @tap="evaluate">开始评估</button>
    </view>

    <view v-if="result" class="result">
      <view>风险等级：{{result.level}}</view>
      <view>建议：{{result.advice}}</view>
    </view>

  </view>
</template>

<script setup>
import { ref } from 'vue'
import { riskApi } from '@/api/risk'

const form = ref({
  age:'',
  height:'',
  weight:'',
  sbp:'',
  dbp:''
})

const result = ref(null)

const evaluate = async () => {
  console.log("点击评估")

  const res = await riskApi(form.value)

  result.value = res

}
</script>