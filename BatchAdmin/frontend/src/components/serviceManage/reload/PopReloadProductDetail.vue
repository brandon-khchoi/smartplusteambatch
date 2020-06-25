<template>
  <div name="modal-corp-search" class="ModalCorpSearch">
    <div class="vue-modal-box">
      <div class="dialog-content">
          <div class="titleWrap" style="display:inline;">
            <h1 style="display:inline; float:left; width:250px">연동 정보 미리보기</h1>
            <p class="btn_search" style="margin-left:10px;" @click="get">새로고침</p>
          </div>
          <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
          <div class="dialog-c-text">
              <div class="popupContentWrap">
                  <div class="popupContent">
                    <article style="overflow-y:scroll;height:500px">

                    <div style="display:inline;">
                      <div style="display:inline; float:left; width:60%">
                        <h2 v-if="productInfoData === ''" style="text-align:center"> 매칭되는 상품정보가 존재하지 않습니다.</h2>
                        <iframe :src="productInfoData.product_detail_url"
                                width="90%" height="650" frameborder="0">
                        </iframe>
                      </div>
                      <div style="display:inline; float:left; width:40%">
                        <ul style="text-align:left;">
                          <li><img :src="productInfoData.product_image_url"></li>
                          <li><h3>{{productInfoData.product_name | tagFilter}}</h3></li>
                          <li><h3>{{productInfoData.product_price | priceFilter}}</h3></li>
                        </ul>
                      </div>
                    </div>

                    </article>
                  </div>
              </div>
          </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import SanitizeHtml from 'sanitize-html'

export default {
  data () {
    return {
      isLoading: false,
      productInfoData: ''
    }
  },
  created () {
    this.get()
  },
  filters: {
    tagFilter: function (val) {
      if (val !== undefined) {
        return '상품명 : ' + SanitizeHtml(val, {
          allowedTags: [],
          allowedAttributes: []
        })
      }
    },
    priceFilter: function (val) {
      if (val !== undefined) { return '가격 : ' + val.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,') + '원' }
    }
  },
  methods: {
    get () {
      let memId = this.data.member_id
      let shopId = this.data.member_mall_no
      let category = this.data.category

      this.isLoading = true
      this.reloadHttp
        .get('/api/v1/shop/' + memId + '/' + shopId + '/category/' + category + '/product/random')
        .then(response => {
          if (response.data.task_status_code === '') {
            this.productInfoData = response.data.data
          } else {
            this.productInfoData = ''
          }
          this.isLoading = false
        })
        .catch(e => {
          this.isLoading = false
          console.log(e)
        })
    }
  },
  props: {
    'data': Object,
    'reloadHttp': Object
  }
}
</script>
