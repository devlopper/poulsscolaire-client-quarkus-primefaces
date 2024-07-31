package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryDto;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.animation.Animation;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

/**
 * Cette classe représente la page de liste de {@link FeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class FeeCategoryListPage extends AbstractPage {

  @Inject
  @Getter
  FeeCategoryController controller;

  @Getter
  PieChartModel pieChartModel;

  @Getter
  BarChartModel barChartModel;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste des rubriques";
    controller.initialize();

    createPieModel();

    createBarModel();
  }

  private void createPieModel() {
    pieChartModel = new PieChartModel();

    List<Number> values = new ArrayList<>();
    values.add(Core.isStringBlank(controller.amountValueStatistics.getPaidAsString()) ? 0
        : Integer.valueOf(controller.amountValueStatistics.getPaidAsString().replaceAll(" ", "")));
    values.add(Core.isStringBlank(controller.amountValueStatistics.getPayableAsString()) ? 0
        : Integer
            .valueOf(controller.amountValueStatistics.getPayableAsString().replaceAll(" ", "")));
    PieChartDataSet dataSet = new PieChartDataSet();

    dataSet.setData(values);

    List<String> bgColors = new ArrayList<>();
    bgColors.add("rgb(255, 99, 132)");
    bgColors.add("rgb(54, 162, 235)");
    dataSet.setBackgroundColor(bgColors);

    ChartData data = new ChartData();
    data.addChartDataSet(dataSet);
    List<String> labels = new ArrayList<>();
    labels.add("Payé");
    labels.add("Reste à payer");
    data.setLabels(labels);

    pieChartModel.setData(data);
  }

  private void createBarModel() {
    barChartModel = new BarChartModel();

    BarChartDataSet barDataSet = new BarChartDataSet();
    barDataSet.setLabel("Paiements");

    List<Number> values = new ArrayList<>();
    values.add(Core.isStringBlank(controller.amountValueStatistics.getPaidAsString()) ? 0
        : Integer.valueOf(controller.amountValueStatistics.getPaidAsString().replaceAll(" ", "")));
    values.add(Core.isStringBlank(controller.amountValueStatistics.getPayableAsString()) ? 0
        : Integer
            .valueOf(controller.amountValueStatistics.getPayableAsString().replaceAll(" ", "")));
    barDataSet.setData(values);

    List<String> bgColor = new ArrayList<>();
    bgColor.add("rgba(255, 99, 132, 0.2)");
    bgColor.add("rgba(255, 159, 64, 0.2)");
    barDataSet.setBackgroundColor(bgColor);

    List<String> borderColor = new ArrayList<>();
    borderColor.add("rgb(255, 99, 132)");
    borderColor.add("rgb(255, 159, 64)");
    barDataSet.setBorderColor(borderColor);
    barDataSet.setBorderWidth(1);

    ChartData data = new ChartData();
    data.addChartDataSet(barDataSet);

    List<String> labels = new ArrayList<>();
    labels.add("Payé");
    labels.add("Reste à payer");
    data.setLabels(labels);
    barChartModel.setData(data);

    // Options
    BarChartOptions options = new BarChartOptions();
    options.setMaintainAspectRatio(false);

    CartesianLinearAxes linearAxes = new CartesianLinearAxes();
    linearAxes.setOffset(true);
    linearAxes.setBeginAtZero(true);
    CartesianLinearTicks ticks = new CartesianLinearTicks();
    linearAxes.setTicks(ticks);
    CartesianScales cartesianScales = new CartesianScales();
    cartesianScales.addYAxesData(linearAxes);
    options.setScales(cartesianScales);

    Title title = new Title();
    title.setDisplay(true);
    title.setText("Paiements");
    options.setTitle(title);

    Legend legend = new Legend();
    legend.setDisplay(true);
    legend.setPosition("top");
    LegendLabel legendLabels = new LegendLabel();
    legendLabels.setFontStyle("italic");
    legendLabels.setFontColor("#2980B9");
    legendLabels.setFontSize(24);
    legend.setLabels(legendLabels);
    options.setLegend(legend);

    // disable animation
    Animation animation = new Animation();
    animation.setDuration(0);
    options.setAnimation(animation);

    barChartModel.setOptions(options);
  }


  public static final String OUTCOME = "feeCategoryListPage";
}
