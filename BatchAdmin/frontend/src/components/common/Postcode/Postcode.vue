<template>
  <div
    class="Postcode"
    v-if="opened"
    @click="closePopup()"
  >
    <div class="post-loading"><div></div><div></div><div></div><div></div></div>
    <div class="wrapper">
      <vue-daum-postcode @complete="result => complete(result)"
        :animation="true"
        :no-shorthand="true"
        :max-suggest-items="3"
        :show-more-h-name="true"
        :hide-map-btn="true"
        />
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import VueDaumPostcode from 'vue-daum-postcode'
Vue.use(VueDaumPostcode)

export default {

  methods: {
    closePopup () {
      this.$emit('close')
    },
    complete (result) {
      this.closePopup()
      this.$emit('complete', result)
    }
  },
  props: {
    opened: {
      type: Boolean,
      default: false
    }
  }
}
</script>
