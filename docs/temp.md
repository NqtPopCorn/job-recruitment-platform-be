```ts
interface ApiDataResponse<T> {
  statusCode: number,
  message: string,
  data: T
}

interface ApiPageResponse<T> {
  statusCode: 200,
  message: string,
  results: T[],
  meta: {
    totalItems: number,
    currentPage: number,
    pageSize:  number,
    totalPages: number
  }
}
```

# Candidate

## 1. GET Page request

> GET /api/v1/candidate?page=1&size=10 (@RequestBody CandidateRequest, Pageable pageable)

```ts
interface CandidateRequest {
  search: string
  location: string
  industry: string
  experience: number
  education: string
  gender: `male` | `female` | `other`
}

interface CandidateResponse {
  id: number,
  avatar: string,
  name: string
  designation: string,
  location: string,
  hourlyRate: number,
  tags: string[],
  category: string,
  gender: string,
  createdAt: string,
  status: boolean,
  socialMedias: SocialMediaResponse[]
}

interface SocialMediaResponse {
  platform: string,
  url: string
}

```

## 2. POST NEW CANDIDATE

> POST /api/v1/candidate (@RequestBody CreateCandidateDto)

```ts
interface CreateCandidateDto {
  userId        : number
  name          : string
  birthday      : string | null
  phone         : string
  industry      : string
  skills        : array
  avatar        : string
  designation   : string
  location      : string
  hourlyRate    : number | null
  description   : string
  experience    : string
  currentSalary : string
  expectedSalary: string
  gender        : string
  language      : array
  educationLevel: string
  socialMedias  :SocialMediaResponse[]
}

```

## 3. GET DETAIL CANDIDATE BY ID
> GET /api/v1/candidate/details/:id

## UPLOAD FILE AND RESUME 


```js
// <-- Upload candidate resume !-->
  @Post('resume/candidate/:id')
  @HttpCode(HttpStatus.CREATED)
  @UseInterceptors(
    FileInterceptor('file', {
      storage: memoryStorage(), // lưu tạm vào memory
    }),
  )
  async uploadResumeFile(
    @Param('id') candidateId: string,
    @UploadedFile() file: Express.Multer.File,
  ) {
    const existingFiles =
      await this.resumeService.getTheNumberOfResumesByCandidateId(candidateId);
    if (existingFiles >= Number(process.env.MAX_RESUME_COUNT)) {
      throw new BadRequestException(
        'Ứng viên đã đạt tối đa 10 CV, không thể upload thêm.',
      );
    }

    // Lưu file từ memory vào disk
    const ext = extname(file.originalname);
    const fileName = ${basename(file.originalname, ext)}-${Date.now()}${ext};
    await fs.promises.writeFile(./images/resumes/${fileName}, file.buffer);

    // Lưu info vào DB
    const res = await this.uploadService.uploadResume(candidateId, fileName);

    return {
      success: true,
      statusCode: HttpStatus.OK,
      message: 'Upload resume ứng viên thành công!',
      data: ResumeResponseDto.builder()
        .withId(res._id.toString())
        .withCandidateId(res.candidateId.toString())
        .withFileName(res.fileName || '')
        .build(),
    };
  }

  @Delete('resume/:id')
  @HttpCode(HttpStatus.OK)
  async deleteResume(
    @Param('id') id: string,
    @Body('filename') filename: string,
  ) {
    await this.uploadService.deleteResume(id, filename);
    return {
      success: true,
      statusCode: HttpStatus.OK,
      message: 'Xóa resume ứng viên thành công!',
    };
  }
```