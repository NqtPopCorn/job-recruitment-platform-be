# Upload company logo and images

```ts
// <--  !-->
  @Get('logo/company/:id')
  @HttpCode(HttpStatus.OK)
  async getLogoOfComanyById(@Param('id') id: string) {
    const res = await this.uploadService.getLogoOfCompanyById(id);
    return {
      success: true,
      statusCode: HttpStatus.OK,
      message: 'Lấy logo công ty thành công!',
      data: res || '',
    };
  }

  @Get('images/company/:id')
  @HttpCode(HttpStatus.OK)
  async getImagesOfComanyById(@Param('id') id: string) {
    const res = await this.uploadService.getImagesOfCompanyById(id);
    return {
      success: true,
      statusCode: HttpStatus.OK,
      message: 'Lấy ảnh công ty thành công!',
      results: res || [],
    };
  }

  @Post('image/company/:companyId')
  @HttpCode(HttpStatus.OK)
  @UseInterceptors(
    FileInterceptor('file', {
      storage: diskStorage({
        destination: './images/companies', // vị trí thư mục lưu ảnh được lưu trên ổ đĩa
        filename: (req, file, cb) => {
          const uniqueSuffix =
            Date.now() + '-' + Math.round(Math.random() * 1e9);
          const ext = extname(file.originalname);
          cb(null, ${file.fieldname}-${uniqueSuffix}${ext});
        },
      }),
    }),
  )
  async uploadCompanyImageFile(
    @Param('companyId') companyId: string,
    @UploadedFile() file: Express.Multer.File,
  ) {
    const filename = file.filename; // ✅ tên file đã chỉnh sửa
    const res = await this.uploadService.uploadImageCompany(
      companyId,
      filename,
    );
    return {
      success: true,
      statusCode: HttpStatus.OK,
      message: 'Upload ảnh công ty thành công!',
      data: res || '',
    };
  }

  @Post('logo/company/:companyId')
  @HttpCode(HttpStatus.OK)
  @UseInterceptors(
    FileInterceptor('file', {
      storage: diskStorage({
        destination: './images/companies', // vị trí thư mục lưu ảnh được lưu trên ổ đĩa
        filename: (req, file, cb) => {
          const uniqueSuffix =
            Date.now() + '-' + Math.round(Math.random() * 1e9);
          const ext = extname(file.originalname);
          cb(null, ${file.fieldname}-${uniqueSuffix}${ext});
        },
      }),
    }),
  )
  async uploadCompanyLogoFile(
    @Param('companyId') companyId: string,
    @UploadedFile() file: Express.Multer.File,
  ) {
    const filename = file.filename; // ✅ tên file đã chỉnh sửa
    const res = await this.uploadService.uploadLogoCompany(companyId, filename);
    return {
      success: true,
      statusCode: HttpStatus.OK,
      message: 'Upload logo công ty thành công!',
      data: res || '',
    };
  }

  @Delete('image/company/:id')
  @HttpCode(HttpStatus.OK)
  async deleteImageCompany(
    @Param('id') id: string,
    @Body('filename') filename: string,
  ) {
    const res = await this.uploadService.deleteImageCompany(id, filename);
    return {
      success: true,
      statusCode: HttpStatus.OK,
      message: 'Xóa ảnh công ty thành công!',
      data: res || {},
    };
  }

  @Delete('logo/company/:id')
  @HttpCode(HttpStatus.OK)
  async deleteLogoCompany(
    @Param('id') id: string,
    @Body('filename') filename: string,
  ) {
    console.log('filename: ', filename);
    const res = await this.uploadService.deleteLogoCompany(id, filename);
    return {
      success: true,
      statusCode: HttpStatus.OK,
      message: 'Xóa logo công ty thành công!',
      data: res || {},
    };
  }
```
```