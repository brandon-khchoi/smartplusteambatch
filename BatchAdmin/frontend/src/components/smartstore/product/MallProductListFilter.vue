<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">

        <!-- modal Header -->
        <div class="dialog-c-title">
          <h2> 검색 결과 내 필터</h2>
          <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        </div>

        <!-- modal Content -->
        <div class="modalTabContainer">
          <div>
            <div>
              <p class="inputWrap">
                <input type="checkbox" name=" " value="" id="allCheckYn" v-model="savedFlagObject.allCheckYn" @click="allCheck()">
                <label class="filterCheck" for="allCheckYn">전체 선택</label><br>
              </p>
            </div><br>

            <div>
              <h3 class="filterTitle">스마트스토어 동기화 상태</h3>
              <span>
                <p class="inputWrap">
                  <input type="checkbox" name=" " value="가격" id="priceYn" v-model="savedFlagObject.syncFlag.product_price_link_yn">
                  <label class="filterCheck" for="priceYn">가격</label>

                  <input type="checkbox" name=" " value="판매상태" id="sellYn" v-model="savedFlagObject.syncFlag.product_sell_link_yn">
                  <label class="filterCheck" for="sellYn">판매상태</label>

                  <input type="checkbox" name=" " value="옵션" id="optionYn" v-model="savedFlagObject.syncFlag.product_option_link_yn">
                  <label class="filterCheck" for="optionYn">옵션</label>

                  <input type="checkbox" name=" " value="상품명" id="productNameYn" v-model="savedFlagObject.syncFlag.product_name_link_yn">
                  <label class="filterCheck" for="productNameYn">상품이름</label>

                  <input type="checkbox" name=" " value="상품상세" id="productDetailYn" v-model="savedFlagObject.syncFlag.product_detail_link_yn">
                  <label class="filterCheck" for="productDetailYn">상품상세</label>
                </p>
              </span>
            </div><br>

            <div>
              <h3 class="filterTitle">상품 연동 상태</h3>
              <div>
                <p class="inputWrap">
                  <input type="checkbox" name=" " value="자사몰에서 연동된 상품" id="mallMatchedLinkYn" v-model="savedFlagObject.connectFlag.product_luna_link_yn">
                  <label class="filterCheck" for="mallMatchedLinkYn">자사몰에서 연동된 상품</label>
                </p>
              </div>
              <div>
                <p class="inputWrap">
                  <input type="checkbox" name=" " value="수기등록상품에서 매칭된 상품" id="unMatchedLinkYn" v-model="savedFlagObject.connectFlag.product_unmatched_link_yn">
                  <label class="filterCheck" for="unMatchedLinkYn">수기등록상품에서 매칭된 상품</label>
                </p>
              </div>
              <div>
                <p class="inputWrap">
                  <input type="checkbox" name=" " value="스마트스토어에서 삭제된 상품" id="deletedYn" v-model="savedFlagObject.connectFlag.smartstore_del_yn">
                  <label class="filterCheck" for="deletedYn">스마트스토어에서 삭제된 상품</label>
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- modal footer 통합어드민 버튼 Css Wrapper -->
        <div>
          <div class="formContainer">
            <article></article>
            <div class="btnWrap">
              <ul>
                <!-- <li><input type="button" value="초기화" class="btn_cancel" @click="filteringInit()"></li> -->
                <li><input type="button" value="적용" class="btn_save" @click="productFiltering()"></li>
              </ul>
            </div>
          </div>
        </div>
      </div>

    </div>
    <!-- <Spinner v-if="isLoading" /> -->
  </div>
</template>

<script>
import { eventBus } from '@/main'

export default {
  name: 'MallProductListFilter',
  data () {
    return {
      filterGuideText: '',
      member_list: []
    }
  },
  props: {
    'savedFlagObject': Object
  },
  computed: {
  },
  watch: {
    savedFlagObject: {
      deep: true,
      handler: function (newVal, oldVal) {
        // console.log('before : ' + JSON.stringify(oldVal.props['allCheckYn']))
        // console.log('after : ' + JSON.stringify(newVal.props['allCheckYn']))
        if (JSON.stringify(oldVal) !== JSON.stringify(newVal)) {
          console.log('다름')
          // this.savedFlagObject.allCheckYn = false
        } else {
          console.log('동일')
          // this.savedFlagObject.allCheckYn = true
        }
        // console.log('after : ' + JSON.stringify(newVal))
        // var setChangedMangerObject = newVal.filter(function (p, idx) {
        //   Object.keys(p).some(function (prop) {
        //     console.log(prop + ' || newVal[' + idx + '] : ' + newVal[idx])
        //   })
        // }) // end of filter
      }
    }
  },
  methods: {
    allCheck () {
      if (this.savedFlagObject.allCheckYn) {
        this.changeAllFlag(false)
      } else {
        // this.filterGuideText = '전체 상품, 전체동기화'
        this.changeAllFlag(true)
      }
    },
    changeAllFlag (flag) {
      /* 전체 checkBox ON/OFF 처리 */
      for (var key in this.savedFlagObject.connectFlag) {
        this.savedFlagObject.connectFlag[key] = flag
      }
      for (var idx in this.savedFlagObject.syncFlag) {
        this.savedFlagObject.syncFlag[idx] = flag
      }
    },
    productFiltering () {
      this.savedFlagObject.filterInitYn = false
      eventBus.$emit('productFiltering', this.savedFlagObject)
      this.$emit('close')
    },
    filteringInit () {
      this.savedFlagObject.filterInitYn = true
      eventBus.$emit('productFiltering', this.savedFlagObject)
      this.$emit('close')
    },
    closeModal () {
      this.$emit('close')
    }
  }
}
</script>
<style scoped>
.filterCheck{
  margin-left: 10px;
  margin-bottom: 10px;
}
.filterTitle{
  margin-bottom: 10px;
}
</style>
