<template>
  <div>
    <p>
      <button v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list"></pagination>

    <div class="row">
      <div v-for="teacher in teachers" class="col-xs-12 col-sm-3 center">
        <div>
          <span class="thumbnail search-thumbnail">
           <img v-show="!teacher.image" class="editable img-responsive editable-click editable-empty"
                src="/static/image/义勇.jpg" v-bind:title="teacher.intro"/>
           <img v-show="teacher.image" class="editable img-responsive editable-click editable-empty"
                v-bind:src="teacher.image" v-bind:title="teacher.intro"/>
          </span>
          <div class="space-4"></div>

          <div class="width-85 label label-info label-xlg arrowed-in arrowed-in-right">
            <div class="inline position-relative">
              <a href="javascript:;" class="user-title-label dropdown-toggle" data-toggle="dropdown">
                <i class="ace-icon fa fa-circle light-green"></i>
                &nbsp;
                <span class="white">{{ teacher.position }}</span>
              </a>
            </div>
          </div>
        </div>

        <div class="space-6"></div>

        <a href="javascript:;" class="text-info bigger-110" v-bind:title="teacher.motto">
          <i class="ace-icon fa fa-user"></i>
          {{ teacher.name }}【{{ teacher.nickname }}】
        </a>

        <div class="space-6"></div>

        <div class="profile-social-links align-center">
          <button v-on:click="edit(teacher)" class="btn btn-white btn-xs btn-info btn-round">
            <i class="ace-icon fa fa-pencil bigger-120"> 编辑</i>
          </button>
          &nbsp;
          <button v-on:click="del(teacher.id)" class="btn btn-white btn-xs btn-warning btn-round">
            <i class="ace-icon fa fa-trash-o bigger-120"> 删除</i>
          </button>
        </div>

        <div class="hr hr16 dotted"></div>
      </div>
    </div>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label for="name" class="col-sm-2 control-label">姓名</label>
                <div class="col-sm-10">
                  <input v-model="teacher.name" type="text" class="form-control" id="name" placeholder="姓名">
                </div>
              </div>
              <div class="form-group">
                <label for="nickname" class="col-sm-2 control-label">昵称</label>
                <div class="col-sm-10">
                  <input v-model="teacher.nickname" type="text" class="form-control" id="nickname" placeholder="昵称">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">头像</label>
                <div class="col-sm-10">
                  <file
                      v-bind:suffixs="['jpg', 'jpeg', 'png']"
                      v-bind:input-id="'image-upload'"
                      v-bind:text="'上传头像'"
                      v-bind:after-upload="afterUpload"></file>
                  <div v-show="teacher.image" class="row">
                    <div class="col-md-4">
                      <!-- img-responsive 图片自适应 -->
                      <img v-bind:src="teacher.image" class="img-responsive">
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label for="position" class="col-sm-2 control-label">职位</label>
                <div class="col-sm-10">
                  <input v-model="teacher.position" type="text" class="form-control" id="position" placeholder="职位">
                </div>
              </div>
              <div class="form-group">
                <label for="motto" class="col-sm-2 control-label">座右铭</label>
                <div class="col-sm-10">
                  <input v-model="teacher.motto" type="text" class="form-control" id="motto" placeholder="座右铭">
                </div>
              </div>
              <div class="form-group">
                <label for="intro" class="col-sm-2 control-label">简介</label>
                <div class="col-sm-10">
                  <textarea v-model="teacher.intro" type="text" class="form-control" id="intro" rows="5"></textarea>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>

</template>

<script>
import Pagination from "../../components/pagination";
import File from "../../components/file";

export default {
  components: {File, Pagination},
  name: "business-teacher",
  data: function () {
    return {
      teacher: {}, // 用于绑定form表单的数据
      teachers: [],
    }
  },
  mounted: function () {
    // sidebar激活样式方法一
    // this.$parent.activeSidebar("business-teacher-sidebar");
    let _this = this;
    _this.$refs.pagination.size = 5;
    _this.list(1);
  },
  methods: {
    /**
     * 点击编辑
     * @param teacher
     */
    edit(teacher) {
      let _this = this;
      _this.teacher = $.extend({}, teacher); // 列表数据复制一份赋值给表单teacher，防止修改表单时把列表也修改
      $("#form-modal").modal("show");//hide
    },
    /**
     * 点击新增
     */
    add() {
      let _this = this;
      //.modal里的modal是内置的方法,用于弹出或关闭模态框
      _this.teacher = {};
      $("#form-modal").modal("show");//hide
    },
    /**
     * 列表查询
     * @param page
     */
    list(page) {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/teacher/list", {
        page: page,
        size: _this.$refs.pagination.size
      }).then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.teachers = resp.content.list;
        _this.$refs.pagination.render(page, resp.content.total);
      });
    },
    /**
     * 点击保存
     */
    save() {
      let _this = this;

      // 保存校验
      if (1 != 1
          || !Validator.require(_this.teacher.name, "姓名")
          || !Validator.length(_this.teacher.name, "姓名", 1, 50)
          || !Validator.length(_this.teacher.nickname, "昵称", 1, 50)
          || !Validator.length(_this.teacher.image, "头像", 1, 100)
          || !Validator.length(_this.teacher.position, "职位", 1, 50)
          || !Validator.length(_this.teacher.motto, "座右铭", 1, 50)
          || !Validator.length(_this.teacher.intro, "简介", 1, 500)
      ) {
        return;
      }
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/teacher/save", _this.teacher).then((response) => {
        Loading.hide();
        let resp = response.data;
        if (resp.success) {
          $("#form-modal").modal("hide");
          _this.list(1);
          Toast.success("保存成功");
        } else {
          Toast.warning(resp.message);
        }
      });
    },
    /**
     * 点击删除
     * @param id
     */
    del(id) {
      let _this = this;
      Confirm.show("确认删除？", "删除后不可恢复，确认删除!", function () {
        _this.$ajax.delete(process.env.VUE_APP_SERVER + "/business/admin/teacher/delete/" + id).then((response) => {
          let resp = response.data;
          if (resp.success) {
            _this.list(1);
            Toast.success("删除成功");
          }
        })
      });
    },
    afterUpload(resp) {
      let _this = this;
      let image = resp.content;
      _this.teacher.image = image;

      console.log("文件上传成功：", image);
    }
  }
}
</script>