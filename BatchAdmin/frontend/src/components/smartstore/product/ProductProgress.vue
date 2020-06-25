<template>
  <div class="bootstrapWrapper" >
    <b-progress :max="propData.searchCnt" show-value :animated="animate"  height="2.1rem">
      <b-progress-bar :value="propData.resultCnt">
        {{message}}
        <strong>{{ this.propData.resultCnt }} / {{ this.propData.searchCnt }}</strong>
      </b-progress-bar>
      <b-progress-bar :value="propData.errCnt" variant="danger" v-if="propData.errCnt" >
        오류
        <strong>{{ this.propData.errCnt }} / {{ this.propData.searchCnt }}</strong>
      </b-progress-bar>
    </b-progress>
  </div>
</template>

<script>
export default {
  data () {
    return {
      animate: true
    }
  },
  mounted () {
    console.log(this.propData)
  },
  props: {
    'propData': Object,
    'message': String
  }
}
</script>
