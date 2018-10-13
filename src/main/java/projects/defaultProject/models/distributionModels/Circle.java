/*
BSD 3-Clause License

Copyright (c) 2007-2013, Distributed Computing Group (DCG)
                         ETH Zurich
                         Switzerland
                         dcg.ethz.ch
              2017-2018, André Brait

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package projects.defaultProject.models.distributionModels;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import sinalgo.configuration.Configuration;
import sinalgo.models.DistributionModel;
import sinalgo.nodes.Position;

/**
 * Distributes the nodes regularly on a circle.
 */
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class Circle extends DistributionModel {

    private double radius;
    private double oneStep;
    private int number;

    @Override
    public void initialize() {
        String parameter = this.getParamString();
        if (parameter.equals("")) {
            if (Configuration.getDimX() < Configuration.getDimY()) {
                this.setRadius(Configuration.getDimX() / 3.0);
            } else {
                this.setRadius(Configuration.getDimY() / 3.0);
            }
        } else {
            this.setRadius(Double.parseDouble(parameter));
        }

        this.setOneStep(360.0 / this.numberOfNodes);
    }

    @Override
    public Position getNextPosition() {
        Position pos = new Position();
        pos.setXCoord((Configuration.getDimX() / 2.0) + (this.getRadius() * Math.cos(Math.toRadians((this.getNumber() * this.getOneStep())))));
        pos.setYCoord((Configuration.getDimY() / 2.0) + (this.getRadius() * Math.sin(Math.toRadians((this.getNumber() * this.getOneStep())))));

        this.setNumber(this.getNumber() + 1);

        return pos;
    }

}
