<template>
  <div id="emptyPage" class="emptyPage">
    <iframe :src=pageUrl width="100%" height="100%"/>
  </div>
</template>

<script>
export default {
  data () {
    return {
      pageUrl: ''
    }
  },
  created () {
    this.pageUrl = this.$route.params.pageurl
  }
}
</script>

<style scoped>
  .emptyPage {
    position: relative;
    width: 100%;
    height: 100%;
  }
</style>
