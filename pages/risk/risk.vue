<template>
  <view class="container">
    <view class="card">
      <view class="title">风险评估</view>
      <view class="subtitle" v-if="isFirstAssessment">首次评估：请完善基础信息与家族史，系统将写入「我的-用户档案」</view>
      <view class="subtitle" v-else>自动评估：系统已读取历史档案，仅需补充少量问卷</view>
    </view>

    <view class="card">
      <view class="section-title">基本信息与最近健康数据</view>
      <input v-model="form.age" class="input" placeholder="年龄" type="number" :disabled="!isFirstAssessment" />

      <template v-if="isFirstAssessment || !autoDataReady.heightWeight">
        <input v-model="form.height" class="input" placeholder="身高 cm" type="digit" />
        <input v-model="form.weight" class="input" placeholder="体重 kg" type="digit" />
      </template>
      <view v-else class="auto-tip">已读取：身高 {{ form.height }} cm，体重 {{ form.weight }} kg</view>

      <template v-if="isFirstAssessment || !autoDataReady.bp">
        <view class="bp-row">
          <input v-model="form.sbp" class="input bp-input" placeholder="收缩压" type="digit" />
          <text class="slash">/</text>
          <input v-model="form.dbp" class="input bp-input" placeholder="舒张压" type="digit" />
        </view>
      </template>
      <view v-else class="auto-tip">已读取：血压 {{ form.sbp }}/{{ form.dbp }} mmHg</view>
    </view>

    <view class="card">
      <view class="section-title">生活习惯问卷</view>
      <picker :range="exerciseOptions" :value="exerciseIndex" @change="onExerciseChange" class="picker">
        <view class="picker-value">每周运动次数：{{ form.exercise || '请选择' }}</view>
      </picker>
      <picker :range="sleepOptions" :value="sleepIndex" @change="onSleepChange" class="picker">
        <view class="picker-value">日均睡眠时长：{{ form.sleep || '请选择' }}</view>
      </picker>
      <picker :range="saltOptions" :value="saltIndex" @change="onSaltChange" class="picker">
        <view class="picker-value">饮食口味：{{ form.salt || '请选择' }}</view>
      </picker>
    </view>

    <view class="card" v-if="isFirstAssessment">
      <view class="section-title">家族史（首次评估必填）</view>
      <view class="family-item" v-for="item in familyItems" :key="item.key">
        <text>{{ item.label }}</text>
        <switch :checked="form.family[item.key]" @change="(e) => onFamilyChange(item.key, e)" color="#3c9cff" />
      </view>
    </view>

    <button class="btn" @tap="evaluate">生成风险评估</button>

    <view v-if="result" class="card result-card">
      <view class="section-title">评估结果</view>

      <view class="result-block">
        <view class="result-head">（1）总评</view>
        <view class="result-row"><text>综合评分：</text><text class="score">{{ result.summary.score }} 分</text></view>
        <view class="result-row"><text>综合风险：</text><text :class="['risk-level', `risk-${result.summary.levelKey}`]">{{ result.summary.level }}</text></view>
        <view class="result-text">当前状态：{{ result.summary.status }}</view>
      </view>

      <view class="result-block">
        <view class="result-head">（2）各项风险分解</view>
        <view class="result-text" v-for="item in result.breakdown" :key="item.name">
          {{ item.name }}：{{ item.score }}分（{{ item.level }}）
        </view>
      </view>

      <view class="result-block">
        <view class="result-head">（3）原因解释</view>
        <view class="result-text" v-for="(item, idx) in result.reasons" :key="`reason-${idx}`">• {{ item }}</view>
      </view>

      <view class="result-block">
        <view class="result-head">（4）行动建议</view>
        <view class="result-text" v-for="(item, idx) in result.actions" :key="`action-${idx}`">• {{ item }}</view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { getHealthListApi } from '@/api/health'

const userInfo = uni.getStorageSync('userInfo') || {}
const familyItems = [
  { key: 'hypertension', label: '高血压家族史' },
  { key: 'diabetes', label: '糖尿病家族史' },
  { key: 'heartDisease', label: '心脏病家族史' },
  { key: 'immuneDisease', label: '免疫系统疾病家族史' }
]

const exerciseOptions = ['0 次', '1 次', '2 次', '3 次及以上']
const sleepOptions = ['< 6 小时', '6-8 小时', '> 8 小时']
const saltOptions = ['清淡', '适中', '偏咸']

const form = reactive({
  age: '',
  height: '',
  weight: '',
  sbp: '',
  dbp: '',
  exercise: '',
  sleep: '',
  salt: '',
  family: {
    hypertension: false,
    diabetes: false,
    heartDisease: false,
    immuneDisease: false
  }
})

const result = ref(null)
const isFirstAssessment = ref(true)
const autoDataReady = reactive({ heightWeight: false, bp: false })

const exerciseIndex = computed(() => exerciseOptions.findIndex((item) => item === form.exercise))
const sleepIndex = computed(() => sleepOptions.findIndex((item) => item === form.sleep))
const saltIndex = computed(() => saltOptions.findIndex((item) => item === form.salt))

const getProfileKey = () => `user_profile_${userInfo.id || 'guest'}`
const toNumber = (value) => Number(value || 0)

const getRiskLevel = (score) => {
  if (score >= 65) return { level: '高风险', key: 'high' }
  if (score >= 35) return { level: '中风险', key: 'medium' }
  return { level: '低风险', key: 'low' }
}

const onExerciseChange = (e) => {
  form.exercise = exerciseOptions[Number(e.detail.value)]
}

const onSleepChange = (e) => {
  form.sleep = sleepOptions[Number(e.detail.value)]
}

const onSaltChange = (e) => {
  form.salt = saltOptions[Number(e.detail.value)]
}

const onFamilyChange = (key, e) => {
  form.family[key] = !!e.detail.value
}

const loadUserProfile = () => {
  const profile = uni.getStorageSync(getProfileKey())
  if (!profile) {
    isFirstAssessment.value = true
    return
  }

  isFirstAssessment.value = false
  form.age = profile.age || ''
  form.family = {
    ...form.family,
    ...(profile.family || {})
  }
}

const saveUserProfile = () => {
  uni.setStorageSync(getProfileKey(), {
    userId: userInfo.id,
    age: form.age,
    family: { ...form.family },
    updatedAt: Date.now()
  })
}

const loadAutoData = async () => {
  if (!userInfo.id) return
  try {
    const res = await getHealthListApi(userInfo.id)
    const list = res?.data || []

    const latestHeight = list.find((item) => item.type === '身高')
    const latestWeight = list.find((item) => item.type === '体重')
    const latestSbp = list.find((item) => item.type === '收缩压')
    const latestDbp = list.find((item) => item.type === '舒张压')

    if (latestHeight && latestWeight) {
      form.height = latestHeight.value
      form.weight = latestWeight.value
      autoDataReady.heightWeight = true
    }

    if (latestSbp && latestDbp) {
      form.sbp = latestSbp.value
      form.dbp = latestDbp.value
      autoDataReady.bp = true
    }
  } catch (e) {
    console.log('读取自动评估数据失败', e)
  }
}

const validateForm = () => {
  if (!form.age || !form.height || !form.weight || !form.sbp || !form.dbp) {
    return '请完成基本信息与健康数据'
  }

  if (!form.exercise || !form.sleep || !form.salt) {
    return '请完成生活习惯问卷'
  }

  return ''
}

const getFamilyRiskCount = () => Object.values(form.family).filter(Boolean).length

const evaluate = () => {
  const msg = validateForm()
  if (msg) {
    uni.showToast({ title: msg, icon: 'none' })
    return
  }

  const height = toNumber(form.height)
  const weight = toNumber(form.weight)
  const sbp = toNumber(form.sbp)
  const dbp = toNumber(form.dbp)
  const bmi = weight / Math.pow(height / 100, 2)
  const familyCount = getFamilyRiskCount()

  const bpScore = sbp >= 160 || dbp >= 100 ? 85 : sbp >= 140 || dbp >= 90 ? 68 : sbp >= 130 || dbp >= 85 ? 42 : 15
  const bmiScore = bmi >= 30 || bmi < 18.5 ? 72 : bmi >= 28 ? 65 : bmi >= 24 ? 44 : 12

  let lifestyleScore = 22
  if (form.exercise === '0 次') lifestyleScore += 35
  else if (form.exercise === '1 次') lifestyleScore += 25
  else if (form.exercise === '2 次') lifestyleScore += 12

  if (form.sleep === '< 6 小时') lifestyleScore += 24
  else if (form.sleep === '> 8 小时') lifestyleScore += 8

  if (form.salt === '偏咸') lifestyleScore += 18
  else if (form.salt === '适中') lifestyleScore += 8

  const metabolicScore = Math.min(95, 20 + familyCount * 14 + (bmi >= 24 ? 8 : 0) + (sbp >= 130 || dbp >= 85 ? 6 : 0))

  const totalScore = Math.round(bpScore * 0.35 + bmiScore * 0.2 + lifestyleScore * 0.25 + metabolicScore * 0.2)

  const bpLevel = getRiskLevel(bpScore)
  const bmiLevel = getRiskLevel(bmiScore)
  const lifestyleLevel = getRiskLevel(lifestyleScore)
  const metabolicLevel = getRiskLevel(metabolicScore)
  const totalLevel = getRiskLevel(totalScore)

  const reasons = []
  if (form.exercise === '0 次' || form.exercise === '1 次') {
    reasons.push(`每周运动 ${form.exercise.replace(' 次', ' 次')}，低于推荐频率，生活方式风险上升。`)
  } else {
    reasons.push(`每周运动 ${form.exercise.replace(' 次及以上', '+ 次')}，达到或接近推荐频率。`)
  }

  reasons.push(form.salt === '偏咸' ? '日常饮食偏咸，长期可能增加血压相关风险。' : '饮食盐分控制较好，对血压风险更友好。')

  if (sbp < 130 && dbp < 85) reasons.push(`当前血压 ${sbp}/${dbp} mmHg，处于正常范围。`)
  else reasons.push(`当前血压 ${sbp}/${dbp} mmHg，已偏高，需连续监测。`)

  reasons.push(`BMI = ${weight} / (${height / 100}²) ≈ ${bmi.toFixed(1)}，${bmi >= 24 ? '体重存在超重风险。' : '体重处于正常范围。'}`)

  if (familyCount === 0) {
    reasons.push('无高血压/糖尿病/心脏病/免疫系统疾病家族史，基础慢病风险较低。')
  } else {
    const tags = familyItems.filter((item) => form.family[item.key]).map((item) => item.label.replace('家族史', ''))
    reasons.push(`存在${tags.join('、')}家族史，建议加强慢病相关指标筛查。`)
  }

  let actions = []
  if (totalLevel.key === 'low') {
    actions = ['继续保持当前作息与体重管理。', '每周运动不少于 3 次。', '每月进行一次健康复盘。']
  } else if (totalLevel.key === 'medium') {
    actions = ['增加运动频率，逐步提高每周活动量。', '控制盐分和油脂摄入，优化饮食结构。', '连续监测 7 天血压或体重变化。']
  } else {
    actions = ['尽快连续监测相关指标，记录每日波动。', '明显异常时及时就医。', '减少高盐高脂饮食，必要时做进一步检查。']
  }

  result.value = {
    summary: {
      score: totalScore,
      level: totalLevel.level,
      levelKey: totalLevel.key,
      status: totalLevel.key === 'high' ? '当前风险较高，建议立即进行强化管理。' : totalLevel.key === 'medium' ? '存在可逆风险，建议尽快干预。' : '整体风险可控，继续保持良好习惯。'
    },
    breakdown: [
      { name: '血压风险', score: bpScore, level: bpLevel.level },
      { name: 'BMI风险', score: bmiScore, level: bmiLevel.level },
      { name: '生活方式风险', score: lifestyleScore, level: lifestyleLevel.level },
      { name: '代谢风险', score: metabolicScore, level: metabolicLevel.level }
    ],
    reasons,
    actions
  }

  if (isFirstAssessment.value) {
    saveUserProfile()
    isFirstAssessment.value = false
  }
}

onMounted(async () => {
  loadUserProfile()
  await loadAutoData()
})
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
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.title {
  font-size: 38rpx;
  font-weight: bold;
}

.subtitle {
  margin-top: 8rpx;
  color: #666;
  font-size: 24rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  margin-bottom: 16rpx;
}

.input {
  height: 80rpx;
  border: 1px solid #dcdfe6;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
  padding: 0 20rpx;
  box-sizing: border-box;
}

.bp-row {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.bp-input {
  flex: 1;
}

.slash {
  margin-bottom: 16rpx;
}

.picker {
  margin-bottom: 16rpx;
}

.picker-value {
  height: 80rpx;
  border: 1px solid #dcdfe6;
  border-radius: 12rpx;
  padding: 0 20rpx;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}

.family-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14rpx 4rpx;
  font-size: 27rpx;
  border-bottom: 1rpx solid #f0f2f5;
}

.auto-tip {
  font-size: 24rpx;
  color: #4a7ecf;
  background: #f2f7ff;
  border-radius: 12rpx;
  padding: 12rpx 16rpx;
  margin-bottom: 16rpx;
}

.btn {
  background: #3c9cff;
  color: #fff;
}

.result-card {
  margin-top: 20rpx;
}

.result-block {
  margin-bottom: 20rpx;
}

.result-head {
  font-size: 28rpx;
  font-weight: 600;
  margin-bottom: 10rpx;
}

.result-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6rpx;
}

.result-text {
  font-size: 25rpx;
  line-height: 1.7;
  color: #555;
}

.score {
  font-weight: 600;
  color: #3c9cff;
}

.risk-level {
  font-weight: 600;
}

.risk-high {
  color: #ef5350;
}

.risk-medium {
  color: #ff9800;
}

.risk-low {
  color: #4caf50;
}
</style>
