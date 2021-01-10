package com.saburto.warehouse.rest.requests;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContainingArticleRequest {

    @NotNull
    private final Integer articleId;

    @NotNull
    private final Integer amountOf;

    @JsonCreator
    public ContainingArticleRequest(@JsonProperty("art_id") Integer articleId,
                                    @JsonProperty("amount_of") Integer amountOf) {
      this.articleId = articleId;
      this.amountOf = amountOf;
    }

    public Integer getArticleId() {
      return articleId;
    }

    public Integer getAmountOf() {
      return amountOf;
    }


}
