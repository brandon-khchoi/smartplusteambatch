<template>
  <div class="content">
    <h1>HISTRIX 모니터링</h1>
    <div class="formContainer">
      <article class="searchResult" >
        <div class="articleWrap">
          <div class="bootstrapWrapper">
            <div style="backgorud:#ffffff">
                <div style="margin:20px;">
                  <b-card-group deck>
                    <b-card>
                      <h3 style="display:inline;">GWAPI01:8081</h3>
                      <div style="float:right;">
                        <b-button variant="primary" @click="onRefresh('firstIframe', src1)">Refresh</b-button>
                      </div>
                      <b-embed
                        id="firstIframe"
                        type="iframe"
                        aspect="16by9"
                        :src="src1"
                        allowfullscreen
                      ></b-embed>
                      <b-card-text></b-card-text>
                      <template v-slot:footer>
                        <small class="text-muted">By 172.31.53.127:8081</small>
                        <b-link :href="src1" target="_blank" class="card-link">link histrix</b-link>
                      </template>
                    </b-card>

                    <b-card>
                      <h3 style="display:inline;">GWAPI01:8082</h3>
                      <div style="float:right;">
                        <b-button variant="primary" @click="onRefresh('secondIframe', src2)">Refresh</b-button>
                      </div>
                      <b-embed
                        id="secondIframe"
                        type="iframe"
                        aspect="16by9"
                        :src="src2"
                        allowfullscreen
                      ></b-embed>
                      <b-card-text>
                      </b-card-text>
                      <template v-slot:footer>
                        <small class="text-muted">By 172.31.53.127:8082</small>
                        <b-link :href="src2" target="_blank" class="card-link">link histrix</b-link>
                      </template>
                    </b-card>
                  </b-card-group>
                </div>
                <div style="margin:20px;">
                  <b-card-group deck>
                    <b-card>
                      <h3 style="display:inline;">GWAPI02:8081</h3>
                      <div style="float:right;">
                        <b-button variant="primary" @click="onRefresh('thirdIframe', src3)">Refresh</b-button>
                      </div>
                      <b-embed
                        id="thirdIframe"
                        type="iframe"
                        aspect="16by9"
                        :src="src3"
                        allowfullscreen
                      ></b-embed>
                      <b-card-text></b-card-text>
                      <template v-slot:footer>
                        <small class="text-muted">By 172.31.100.149:8081</small>
                        <b-link :href="src3" target="_blank" class="card-link">link histrix</b-link>
                      </template>
                    </b-card>

                    <b-card>
                      <h3 style="display:inline;">GWAPI02:8082</h3>
                      <div style="float:right;">
                        <b-button variant="primary" @click="onRefresh('fourthIframe', src4)">Refresh</b-button>
                      </div>
                      <b-embed
                        id="fourthIframe"
                        type="iframe"
                        aspect="16by9"
                        :src="src4"
                        allowfullscreen
                      ></b-embed>
                      <b-card-text></b-card-text>
                      <template v-slot:footer>
                        <small class="text-muted">By 172.31.100.149:8082</small>
                        <b-link :href="src4" target="_blank" class="card-link">link histrix</b-link>
                      </template>
                    </b-card>
                  </b-card-group>
                </div>
            </div>

          </div>

        </div>
      </article>
    </div>
    <v-dialog />
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>

export default {
  name: 'HistrixList',
  components: {
  },
  data () {
    return {
      isLoading: false,
      userCode: 1,
      userNo: this.$store.getters.getUser.luna_no,
      src1: 'http://172.31.53.127:8081/hystrix/monitor?stream=http%3A%2F%2F172.31.53.127%3A8081%2Fhystrix.stream&title=GWAPI01%3A8081',
      src2: 'http://172.31.53.127:8082/hystrix/monitor?stream=http%3A%2F%2F172.31.53.127%3A8082%2Fhystrix.stream&title=GWAPI01%3A8082',
      src3: 'http://172.31.100.149:8081/hystrix/monitor?stream=http%3A%2F%2F172.31.100.149%3A8081%2Fhystrix.stream&title=GWAPI02%3A8081',
      src4: 'http://172.31.100.149:8082/hystrix/monitor?stream=http%3A%2F%2F172.31.100.149%3A8082%2Fhystrix.stream&title=GWAPI02%3A8082'

    }
  },
  created () {
    // this.get()
  },
  methods: {
    onRefresh (div, src) {
      // eslint-disable-next-line no-undef
      $('#' + div).attr('src', src)
      console.log(div, src)
    }
  },
  props: {
    userMenuAuth: Object
  }
}
</script>

<style scoped lang="scss">

</style>
